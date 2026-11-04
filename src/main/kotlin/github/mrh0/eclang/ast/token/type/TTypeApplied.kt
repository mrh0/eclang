package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.ect2.GenericRecord
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** `List<Int>`: a generic record with its type arguments. */
class TTypeApplied(location: Loc, private val name: String, private val args: List<ITok>) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val generic = cd.lookupType(location, name) as? GenericRecord ?: throw EcError(location, "$name has no type parameters, so write it without `<...>`.")
        val type = applyGeneric(cd, location, generic, args)
        return type to IRType(location, type)
    }

    override fun toString() = "TTypeApplied($name, $args)"
}

/** [generic] with the type arguments [args]: an instance of it, or an `Applied` type while one of them is still generic. */
internal fun applyGeneric(cd: CompileData, location: Loc, generic: GenericRecord, args: List<ITok>): Type {
    if (args.size != generic.params.size)
        throw EcError(location, "${generic.name} takes ${generic.params.size} type argument${if (generic.params.size == 1) "" else "s"} (${generic.params.joinToString(", ")}), but ${args.size} were given.")
    val types = args.map { it.process(cd, Types.None).first }
    types.forEach { if (!cd.types.hasTypeParam(it)) cd.storable(location, it, "a type argument of ${generic.name}") }
    return cd.types.apply(generic, types)
}

/**
 * A single name such as `T` among the type arguments of `List<T>`: a type parameter unless a type has that name. Only
 * short names are taken this way, so that a misspelled type name is still an error.
 */
class TTypeArgument(location: Loc, private val name: String) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val type = cd.genericContext?.get(name) ?: cd.types.lookup(cd.module.name, name) ?: cd.types.typeParam(name)
        return type to IRType(location, type)
    }

    override fun toString() = "TTypeArgument($name)"

    companion object {
        /** `T`, `K`, `V2`: the names that may be type parameters without `<...>` around them. */
        fun isShort(name: String) = Regex("[A-Z][0-9]?").matches(name)
    }
}
