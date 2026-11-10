package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.NonValueTok
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRPass
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.FixedArray
import github.mrh0.ect2.GenericRecord
import github.mrh0.ect2.Record
import github.mrh0.ect2.Tuple
import github.mrh0.ect2.Union
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** `rec Name extends Parent as` + fields, or `rec Name<T> as`: a generic record, made once for each `Name<Int>` used. */
class TTypeRecord(
    location: Loc, val name: String, val names: List<String>, val types: List<ITok>, private val extending: String?, private val defaults: List<ITok?> = listOf(),
    private val typeParams: List<String> = listOf(), private val layout: TLayout = TLayout(), private val widths: List<ITok?> = listOf(),
    /** The type parameters written `mut T`: see [GenericRecord.mutParams]. */
    private val mutTypeParams: Set<String> = setOf(),
) : NonValueTok(location), ITypeDeclaration {
    override val parentName get() = extending

    override fun declareType(cd: CompileData) {
        if (layout.isUnion && defaults.any { it != null }) throw EcError(location, "The fields of union $name share their memory, so they can't have default values. A literal gives one field: `$name { field = value }`.")
        if (typeParams.isNotEmpty()) return declareGeneric(cd)
        val layout = layout.resolve(cd, location)
        val parent = extending?.let {
            val p = cd.lookupType(location, it)
            if (p !is Record || p.isExternal) throw EcError(location, "$name can only extend a record declared in eclang, but $it is not one.")
            // A `Cat` must mean the same kind of thing as the `Animal` it fits in: both a pointer, or both a value.
            if (p.layout.isRef != layout.isRef) throw EcError(location, if (p.layout.isRef) "${p.name} is a `ref rec`, so a record that extends it must be one too: write `ref rec $name extends ${p.name}`."
                else "$name is a `ref rec`, but ${p.name} isn't, so a $name couldn't be used where a ${p.name} is expected: a ${p.name} is a value, and a $name a pointer. Make both `ref rec`, or neither.")
            // A ${p.name}'s fields can't change, which a $name used as a ${p.name} has to keep true.
            if (layout.isMut && !p.isMutable) throw EcError(location, "$name is a `mut rec`, but it extends ${p.name}, whose fields are fixed: $name can be used where ${p.name} is expected, and there its fields must stay as they are. " +
                "Remove `mut` from $name, or declare `mut ${if (p.layout.isRef) "ref " else ""}rec ${p.name}` too.")
            p
        }
        // A pointer to a child record is used as a pointer to its parent, so the two have to start alike.
        if (parent != null && !(parent.layout.isDefault && layout.isDefault))
            throw EcError(location, "`packed` and `align(n)` records can't take part in `extends`, because a child must be laid out like the start of its parent. Give $name a field of type ${parent.name} instead.")
        checkNewTypeName(cd, location, name)
        cd.claimCName(location, cd.module.cName(name), "record '$name'", renamable = false)
        cd.types.record(cd.module.name, name, parent, layout = layout)
    }

    /**
     * Rejects a record that contains itself by value (`a: A`, or through other records, unions and tuples): it
     * would be infinitely large. A pointer (`@A?`) or an array breaks the cycle.
     */
    fun checkFinite(cd: CompileData) {
        if (typeParams.isEmpty()) checkFinite(declared(cd, location, name) as Record)
    }

    private fun checkFinite(record: Record) {
        val seen = HashSet<Type>()
        /** The fields leading from [t] back to [record], e.g. ["a"] or ["b", "a"], or null. */
        fun path(t: Type): List<String>? = when (t) {
            is Record -> if (!t.isDefined || !seen.add(t)) null else t.fields.entries.firstNotNullOfOrNull { (field, ft) ->
                if (ft === record) listOf(field) else path(ft)?.let { listOf(field) + it }
            }
            is Union -> t.members.firstNotNullOfOrNull { if (it === record) listOf() else path(it) }
            is Tuple -> t.elements.firstNotNullOfOrNull { if (it === record) listOf() else path(it) }
            is FixedArray -> if (t.element === record) listOf() else path(t.element)
            else -> null
        }
        val cycle = path(record) ?: return
        val first = cycle[0]
        val holder = record.fields[first]?.let { if (it is Record) it.name else record.name } ?: record.name
        throw EcError(location, "Record ${record.name} contains itself (through ${cycle.joinToString(".", "${record.name}.")}), so it would be infinitely large. " +
            "Make $holder a `ref rec $holder`, whose name means a pointer to it, so that `$first: $holder?` is a pointer; or store a pointer here: `$first: @$holder?`.")
    }

    override fun defineType(cd: CompileData) {
        if (typeParams.isNotEmpty()) return checkGenericFields(cd)
        val record = declared(cd, location, name)
        defineFields(cd, location, record, names, types, widths)
        setDefaults(cd, record as Record)
    }

    private fun setDefaults(cd: CompileData, record: Record) {
        val given = names.indices.mapNotNull { i -> defaults.getOrNull(i)?.let { testIdentifier(location, names[i]) to it } }.toMap()
        if (given.isNotEmpty()) cd.recordDefaults[record] = given
    }

    private fun declareGeneric(cd: CompileData) {
        if (extending != null) throw EcError(location, "A generic record can't extend another record yet. Put the fields of $extending in $name, or give it a field of type $extending.")
        typeParams.groupBy { it }.values.firstOrNull { it.size > 1 }?.let { throw EcError(location, "$name has two type parameters called '${it[0]}'.") }
        checkNewTypeName(cd, location, name)
        val module = cd.module
        mutTypeParams.firstOrNull { it !in typeParams }?.let { throw EcError(location, "BUG: `mut $it` isn't a type parameter of $name.") }
        cd.types.genericRecord(module.name, name, typeParams, layout.isRef, mutTypeParams) { args -> cd.inModule(module) { instantiate(cd, cd.types.lookupIn(module.name, name) as GenericRecord, args) } }
    }

    /** Reports unknown types in the fields where they are written, also when no code uses the record. */
    private fun checkGenericFields(cd: CompileData) {
        val saved = cd.genericContext
        cd.genericContext = typeParams.associateWith { cd.types.typeParam(it) }
        try {
            types.forEach { it.process(cd, Types.None) }
        } finally {
            cd.genericContext = saved
        }
    }

    /** The record `Name<args>`: a record like any other, with its own C struct. */
    private fun instantiate(cd: CompileData, generic: GenericRecord, args: List<Type>): Record {
        if (cd.instantiating >= MAX_DEPTH) throw EcError(location, "$name<...> keeps making bigger instances of itself, through its field types, so it would never end. Use the same type arguments in its fields.")
        val text = args.joinToString("_") { cText(it.toString()) }
        val fits = name.length + text.length < MAX_C_NAME
        val cBase = if (fits) "${name}_$text" else "${name}_i${generic.instances.size}"
        cd.claimCName(location, cd.module.cName(cBase), "the generic record $name<...>", renamable = false)
        checkMutArguments(cd, location, generic, args)
        val record = cd.types.recordInstance(generic, args, cBase, layout.resolve(cd, location))
        val saved = cd.genericContext
        cd.genericContext = record.typeBindings
        cd.instantiating++
        try {
            defineFields(cd, location, record, names, types, widths)
        } finally {
            cd.instantiating--
            cd.genericContext = saved
        }
        setDefaults(cd, record)
        checkFinite(record)
        return record
    }

    /** [type]'s text as part of a C name: `@Point[]` is `PPointA`. */
    private fun cText(type: String) = type.replace("@", "P").replace("[]", "A").replace("?", "N").replace(Regex("[^a-zA-Z0-9]+"), "_")

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        declareType(cd)
        defineType(cd)
        return Types.None to IRPass(location, null)
    }

    override fun toString(): String {
        return "TTypeRecord($name)"
    }

    private companion object {
        /** How deep instances may make instances of each other, for `rec A<T> as next: @A<@T>?`, which would never end. */
        const val MAX_DEPTH = 32
        /** A longer C name than this falls back to a number. */
        const val MAX_C_NAME = 48
    }
}

/**
 * `rec Box<mut T>`: rejects `Box<Point>` when Point's fields can't change, as the code written for a Box may change
 * fields of its T. [location] is where the instance is asked for.
 */
fun checkMutArguments(cd: CompileData, location: Loc, generic: GenericRecord, args: List<Type>) {
    generic.params.forEachIndexed { i, param ->
        if (param !in generic.mutParams) return@forEachIndexed
        val arg = args.getOrNull(i) ?: return@forEachIndexed
        cd.types.fixedRecord(arg)?.let { r -> throw EcError(location, mutArgumentProblem("${generic.name}<...>'s type parameter `mut $param`", arg, r)) }
    }
}

/** Why [arg], which is or points to [record], can't stand for a `mut T` ([what]). */
fun mutArgumentProblem(what: String, arg: Type, record: Record): String =
    "$what must be a type whose fields can change, but $arg${if (arg is Record) "" else " is or points to ${record.name}, which"} isn't a `mut rec`: its fields are fixed once it is made. " +
        "Declare `mut ${if (record.layout.isRef) "ref " else ""}rec ${record.name}`, or remove `mut` from the type parameter if the code only reads the fields."
