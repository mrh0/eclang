package github.mrh0.eclang.ast.token.accessor

import github.mrh0.eclang.ir.mayBeNullInC
import github.mrh0.eclang.ir.IRCCast
import github.mrh0.eclang.ir.IRCNotNull
import github.mrh0.eclang.ir.accessor.IRArrayData
import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.IAssignable
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Place
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.unnested
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.ast.token.op.Ops
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.Lifetimes
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.error.EcUndefinedAccessorPropertyError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.accessor.IRAccessorNamed
import github.mrh0.eclang.ir.accessor.IRFixedData
import github.mrh0.eclang.ir.accessor.IRFixedStorage
import github.mrh0.eclang.ir.accessor.IRSafeAccess
import github.mrh0.eclang.ir.data.IRNumber
import github.mrh0.eclang.output.c.CTypes
import github.mrh0.eclang.util.Util
import github.mrh0.eclang.ast.token.op.TReadPointer
import github.mrh0.ect2.*

/** Whether [tok], a field or element, is inside a `packed` record, so it may be at any address. */
internal fun inPacked(tok: ITok): Boolean = when (val t = tok.unnested()) {
    is TAccessorNamed -> t.unaligned
    is TAccessor -> t.unaligned
    else -> false
}

/** Whether a [t] needs no alignment, so a pointer to it is fine at any address. */
internal fun alignedAnywhere(t: Type): Boolean = when (t) {
    Types.Char, Types.UChar, Types.Bool -> true
    is Distinct -> alignedAnywhere(t.base)
    is FixedArray -> alignedAnywhere(t.element)
    is Record -> t.layout.packed && t.layout.align == null
    else -> false
}

/** Rejects a pointer into [tok], a [t] in a `packed` record, when that pointer might not be aligned. [what] names the pointer. */
internal fun checkViewable(location: Loc, tok: ITok, t: FixedArray, what: String) {
    if (inPacked(tok) && !alignedAnywhere(t.element))
        throw EcError(location, "$what would point to ${t.element}s inside a `packed` record, which may be at any address, where C can't use such a pointer. Copy the array into a variable first: `val copy = ...`.")
}

/** Rejects a pointer into [tok], a [t], when it is a temporary value that is gone after the statement. */
internal fun checkStored(location: Loc, tok: ITok, t: FixedArray, what: String) {
    val e = tok.unnested()
    if (e !is TNamed && e !is TAccessorNamed && e !is TAccessor && e !is TReadPointer)
        throw EcError(location, "This $t is a temporary value, so it can't be $what: its elements are gone after this statement. Store it in a variable first.")
}

/**
 * Rejects reading the pointer [tok] holds, when [tok] is a field of a `union` that can hold a pointer: the union may hold
 * another of its fields, whose bits make no pointer. Writing the field is fine; reading it needs `unsafe`.
 */
internal fun checkUnionRead(cd: CompileData, location: Loc, tok: ITok) {
    val e = tok.unnested() as? TAccessorNamed ?: return
    val union = e.pointerOfUnion ?: return
    if (cd.isUnsafe) return
    throw EcError(location, "Reading '.${e.name}' of union ${union.name} isn't checked: the union may hold another of its fields, whose bits " +
        "make no valid ${union.field(e.name)}. Keep a field next to it that says which one it holds, and once you have checked that, " +
        "write `unsafe x.${e.name}`.")
}

/** Reads the field [name] of [record] in [base]. A `T[N]` field is a C array, which is read as the struct holding a `T[N]`. */
internal fun readField(location: Loc, base: IIR, record: Record?, name: String, type: Type, throughPointer: Boolean): IIR {
    val access = IRAccessorNamed(location, base, record?.let { CTypes.fieldPath(it, name) } ?: name, throughPointer)
    return if (record != null && CTypes.fixedOf(type) != null) IRFixedStorage(location, access, type, record.layout.packed) else access
}

/**
 * `x.name`: a record field (through a pointer too), `.len`/`.data` of arrays and Strings, `.first` of tuples, or
 * the global `name` of module x. With [safe] it is `x?.name`: null when x is null, otherwise the field (the
 * result is nullable). A `T[N]` has `.len`, its length, and `.data`, a pointer to its elements.
 */
class TAccessorNamed(location: Loc, val expr: ITok, val name: String, private val safe: Boolean = false) : Tok(location), IAssignable {
    override fun pointerFlow(): PointerFlow = PointerFlow.From(expr)

    /** Whether the last compiled access read the field through a pointer (`p.x` with p a `@Point`); see Lifetimes. */
    var throughPointer = false
        private set

    /** Whether the last compiled access was `.data` of a `T[N]` that isn't behind a pointer: it points into x (see Lifetimes). */
    var viewsValue = false
        private set

    /** Whether the field may be at any address, being inside a `packed` record. */
    var unaligned = false
        private set

    /** When the last compiled access was a field of a `union` that can hold a pointer: that union (see [checkUnionRead]). */
    var pointerOfUnion: Record? = null
        private set

    /** When the last compiled access read a bit-field: the record it is in. */
    var bitFieldOf: Record? = null
        private set

    val fixedName get() = Util.testIdentifier(location, name)

    /** The global `name` of module x, when x names a module (and no variable). */
    private fun moduleGlobal(cd: CompileData): github.mrh0.eclang.context.state.IVar? {
        if (safe || expr !is TNamed || cd.findVar(expr.name) != null) return null
        return cd.findModule(expr.name)?.let { m -> cd.getVarIn(location, m, Util.testIdentifier(location, name)) }
    }

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val fixedName = Util.testIdentifier(location, name)
        moduleGlobal(cd)?.let { v -> return v.getType() to v.toIR(location, cd, hint) }
        val op = if (safe) "?." else "."
        val (t, ir) = cd.usable(location, expr.process(cd, Types.None), "`$op$name`")
        if (!safe) return field(cd, t, fixedName).let { (type, throughPointer) ->
            this.throughPointer = throughPointer
            if (throughPointer) Lifetimes.checkThrough(cd, location, expr)
            checkUnionRead(cd, location, this)
            if (type === cd.types.pool) Lifetimes.checkPoolField(cd, location, this)
            arrayOf(t)?.let { fixed ->
                if (fixedName == "len") return type to IRNumber(location, fixed.size, Types.Size)
                checkData(fixed)
                return type to IRFixedData(location, ir, fixed, throughPointer)
            }
            val read = arrayData(t, fixedName, ir, type) ?: readField(location, ir, recordOf(t), fixedName, type, throughPointer)
            type to checkedFromC(cd, t, read, type)
        }

        if (t !is Union || !t.isNullable) throw EcError(location, "A value of type $t is never null, so `?.` is not needed. Write `.$name` instead.")
        val nonNull = cd.types.nonNull(t)
        val (fieldType, throughPointer) = field(cd, nonNull, fixedName)
        checkUnionRead(cd, location, this)
        val result = cd.storable(location, cd.types.union(fieldType, Types.Null), "`?.$name`")
        return result to IRSafeAccess(location, ir, t, nonNull, fixedName, fieldType, result, throughPointer)
    }

    override fun place(cd: CompileData): Place {
        val fixedName = Util.testIdentifier(location, name)
        moduleGlobal(cd)?.let { v ->
            val readOnly = if (v.canAssign()) null else "'${(expr as TNamed).name}.$name' is ${v.kind()}, so it can't be assigned a new value."
            return Place(v.getType(), v.toIR(location, cd, Types.None), readOnly)
        }
        if (safe) throw EcError(location, "Can't assign through `?.`: when the value is null there is nothing to assign to. Check it first with `if x != null do`.")
        val e = expr.unnested()
        val base = if (e is IAssignable) e.place(cd)
            else cd.usable(location, e.process(cd, Types.None), "`.$name`").let { (t, ir) ->
                Place(t, ir, "This is a temporary value, so changing its field `.$name` would have no effect. Store it in a `var` first.")
            }
        cd.usable(location, base.type to base.ir, "`.$name`")
        val (type, throughPointer) = field(cd, base.type, fixedName)
        this.throughPointer = throughPointer
        val fixed = recordOf(base.type)?.takeIf { !it.isMutable }
        val readOnly = when {
            fixed != null -> fixedField(fixed, name)
            throughPointer -> null
            base.type === Types.String || base.type is ArrayType || base.type is FixedArray ->
                "`.$name` can't be assigned: it describes the memory this ${base.type} points to. Make a new value instead, e.g. a slice `x[0..<n]`."
            else -> base.readOnly
        }
        arrayOf(base.type)?.let { fixed ->
            if (fixedName == "data") {
                checkData(fixed)
                return Place(type, IRFixedData(location, base.ir, fixed, throughPointer), readOnly)
            }
        }
        // Writing through a pointer reads the pointer: a C struct's field gets its NULL check (see Place.value).
        if (throughPointer) checkUnionRead(cd, location, e)
        val owner = if (throughPointer) base.value else base.ir
        val field = arrayData(base.type, fixedName, owner, type) ?: readField(location, owner, recordOf(base.type), fixedName, type, throughPointer)
        return Place(type, field, readOnly, value = checkedFromC(cd, base.type, field, type))
    }

    /**
     * [read], the field of a value of type [written], with a NULL check when it is a C struct's pointer field, which C
     * can't promise isn't NULL. A nullable pointer field is cast to its eclang type instead, which drops a `const` of
     * its C declaration (`const char* name`), as eclang has none.
     */
    private fun checkedFromC(cd: CompileData, written: Type, read: IIR, type: Type): IIR {
        val record = recordOf(written)
        return when {
            record == null || !record.isExternal -> read
            mayBeNullInC(type) -> IRCNotNull(location, read, type, "${record.name}.$name", field = true)
            cd.functions.pointerLike(type) -> IRCCast(location, read, type)
            else -> read
        }
    }

    /** `.data` of [value], a String or `T[]` (of type [written]), which is never null; null for any other field. */
    private fun arrayData(written: Type, fixedName: String, value: IIR, type: Type): IIR? {
        val plain = Ops.base(written)
        if (fixedName != "data" || !(plain === Types.String || (plain is ArrayType && !plain.isC))) return null
        return IRArrayData(location, readField(location, value, null, fixedName, type, false), type)
    }

    /** The record a value of type [written] is, or points to. */
    private fun recordOf(written: Type): Record? = Ops.base(written).let { (if (it is Pointer) it.target else it) as? Record }

    /** The `T[N]` a value of type [written] is, or points to. */
    private fun arrayOf(written: Type): FixedArray? = Ops.base(written).let { (if (it is Pointer) it.target else it) as? FixedArray }

    /** `x.data` of [fixed]: a pointer to the elements of x. */
    private fun checkData(fixed: FixedArray) {
        if (throughPointer) return
        checkStored(location, expr, fixed, "used through `.data`")
        checkViewable(location, expr, fixed, "`.data`")
        viewsValue = true
    }

    /** The type of field [name] of a value of type [t], and whether it's read through a pointer. */
    private fun field(cd: CompileData, written: Type, name: String): Pair<Type, Boolean> {
        val t = if (written is Distinct) written.base else written
        val throughPointer = t is Pointer && (t.target is Declared || t.target is Tuple || t.target is FixedArray)
        val target = if (throughPointer) (t as Pointer).target else t
        viewsValue = false
        bitFieldOf = (target as? Record)?.takeIf { name in it.bitWidths }
        unaligned = (target is Record && target.layout.packed) || (!throughPointer && inPacked(expr))
        val fieldType: Type = when {
            target is Declared -> target.field(name)
                ?: throw EcError(location, "${target.name} has no field '$name'.${CompileData.suggestion(name, target.fields.keys)}")
            target === Types.String || (target is ArrayType && !target.isC) -> when (name) {
                "len" -> Types.Size
                "data" -> cd.types.cArray(if (target === Types.String) Types.Char else (target as ArrayType).element)
                else -> throw EcUndefinedAccessorPropertyError(location, name, t)
            }
            target is FixedArray -> when (name) {
                "len" -> Types.Size
                "data" -> cd.types.cArray(target.element)
                else -> throw EcError(location, "A $target has `.len` and `.data`, but no '$name'.")
            }
            target is Tuple -> target.elements.getOrNull((0 until target.elements.size).firstOrNull { Util.getTupleEntryName(it, 0) == name } ?: -1)
                ?: throw EcError(location, "$target has no part '$name'. Its parts are ${target.elements.indices.joinToString { "`.${Util.getTupleEntryName(it, 0)}`" }}.")
            target is Union && target.isNullable ->
                throw EcError(location, "Can't read '.$name' because the value may be null. Check it first with `if x != null`, use `x?.$name` (null when x is null) or `x!.$name` (stops the program when x is null).")
            target is Union -> throw EcError(location, "Can't read '.$name' of a $t; narrow it first, e.g. `if x is ${target.members.first()} do`.")
            else -> throw EcUndefinedAccessorPropertyError(location, name, t)
        }
        pointerOfUnion = (target as? Record)?.takeIf { it.isUnion && (Lifetimes.holdsPointer(fieldType) || fieldType is FunctionType) }
        return fieldType to throughPointer
    }

    override fun toString(): String {
        return "TAccessorNamed($expr, ${if (safe) "?" else ""}$name)"
    }
}

/** Why field [field] of a [record] that isn't a `mut rec` can't be assigned (see Record.isMutable). */
fun fixedField(record: Record, field: String): String {
    val name = record.generic?.name ?: record.name
    val ref = record.layout.isRef || record.generic?.isRef == true
    val kind = if (record.isUnion) "union" else if (ref) "ref rec" else "rec"
    val copy = when {
        record.isUnion -> "`$name { $field = value }`"
        ref -> "`new $name { ...@old, $field = value }`"
        else -> "`{ ...old, $field = value }`"
    }
    if (record.isExternal) {
        val declared = if (record.isUnion) "union rec" else "rec"
        return "${record.name} is a C type declared without `mut`, so eclang code can't change its fields. " +
            "Write `declare mut $declared $name` if it is meant to change in place, or make a new one with the change, $copy."
    }
    return "${record.name} isn't a `mut $kind`: its fields are fixed once it is made. " +
        "Make a new one with the change instead, $copy, or declare `mut $kind $name` to change its fields in place."
}
