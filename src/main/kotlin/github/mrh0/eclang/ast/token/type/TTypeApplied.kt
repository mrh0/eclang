package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.NonValueTok
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.ect2.AtomLiteral
import github.mrh0.ect2.GenericRecord
import github.mrh0.ect2.Record
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types
import github.mrh0.ect2.Union

/** `List<Int>`: a generic record with its type arguments. */
class TTypeApplied(location: Loc, private val name: String, private val args: List<ITok>) : NonValueTok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        if (name == OMIT && !cd.typeDeclared(name)) return omit(cd).let { it to IRType(location, it) }
        val generic = cd.lookupType(location, name) as? GenericRecord ?: throw EcError(location, "$name has no type parameters, so write it without `<...>`.")
        val type = applyGeneric(cd, location, generic, args).let { if (generic.isRef) cd.types.pointer(it) else it }
        return type to IRType(location, type)
    }

    /**
     * `Omit<R, :a, :c>`: the atoms of the enum R (a union of atoms) without :a and :c. A removed type may also be a union of
     * atoms (`Omit<Dir, Vertical>`). Each must be in R, so a misspelled atom is an error. `Omit` is a contextual name:
     * a type of the program called `Omit` takes precedence.
     */
    private fun omit(cd: CompileData): Type {
        if (args.size < 2) throw EcError(location, "`Omit` takes an enum and the atoms to leave out of it: `Omit<Dir, :up, :down>`.")
        val base = args[0].process(cd, Types.None).first
        val members = atomsOf(cd, base, "The enum of `Omit<R, ...>`")
        val removed = args.drop(1).flatMap { atomsOf(cd, it.process(cd, Types.None).first, "What `Omit` leaves out") }
        removed.firstOrNull { it !in members }?.let {
            throw EcError(location, "$it isn't one of the atoms of $base, so `Omit` can't leave it out. Check its spelling.")
        }
        val left = members - removed.toSet()
        if (left.isEmpty()) throw EcError(location, "`Omit` leaves out every atom of $base, so no value would fit the type. Leave out fewer atoms.")
        return cd.types.union(left)
    }

    /** The atoms of [t], which must be an atom or a union of atoms; [what] names it in the error. */
    private fun atomsOf(cd: CompileData, t: Type, what: String): List<AtomLiteral> {
        if (cd.types.hasTypeParam(t))
            throw EcError(location, "$what is $t, which is only known when the function is called; `Omit` needs the atoms themselves, such as `:a | :b` or a `type` naming them.")
        val members = if (t is Union) t.members else listOf(t)
        if (members.all { it is AtomLiteral }) return members.map { it as AtomLiteral }
        throw EcError(location, "$what must be an atom or a union of atoms (an enum such as `type Dir = :up | :down`), but it is $t" +
            (members.firstOrNull { it !is AtomLiteral }?.takeIf { t is Union }?.let { ", which also holds $it" } ?: "") + ".")
    }

    override fun toString() = "TTypeApplied($name, $args)"

    companion object {
        /** `Omit<R, :a>`: an enum without some of its atoms. Not a keyword. */
        const val OMIT = "Omit"
    }
}

/**
 * What the name of [t] means in a type: a pointer to it for a `ref rec`, which is only used through one, and [t] otherwise.
 * A record literal and `new Name` mean the record itself, so they look the name up without this.
 */
internal fun refType(cd: CompileData, t: Type): Type = if (t is Record && t.layout.isRef) cd.types.pointer(t) else t

/** [generic] with the type arguments [args]: an instance of it, or an `Applied` type while one of them is still generic. */
internal fun applyGeneric(cd: CompileData, location: Loc, generic: GenericRecord, args: List<ITok>): Type {
    if (args.size != generic.params.size)
        throw EcError(location, "${generic.name} takes ${generic.params.size} type argument${if (generic.params.size == 1) "" else "s"} (${generic.params.joinToString(", ")}), but ${args.size} were given.")
    val types = args.map { it.process(cd, Types.None).first }
    types.forEach { if (!cd.types.hasTypeParam(it)) cd.storable(location, it, "a type argument of ${generic.name}") }
    checkMutArguments(cd, location, generic, types)
    return cd.types.apply(generic, types)
}

/**
 * A single name such as `T` among the type arguments of `List<T>`: a type parameter unless a type has that name. Only
 * short names are taken this way, so that a misspelled type name is still an error.
 */
class TTypeArgument(location: Loc, private val name: String) : NonValueTok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val type = cd.genericContext?.get(name) ?: cd.types.lookup(cd.module.name, name)?.let { refType(cd, it) } ?: cd.types.typeParam(name)
        return type to IRType(location, type)
    }

    override fun toString() = "TTypeArgument($name)"

    companion object {
        /** `T`, `K`, `V2`: the names that may be type parameters without `<...>` around them. */
        fun isShort(name: String) = Regex("[A-Z][0-9]?").matches(name)
    }
}
