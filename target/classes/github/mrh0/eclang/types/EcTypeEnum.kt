package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc

@Deprecated("Enum is effectively a duplicate of Union")
class EcTypeEnum private constructor(private val types: Set<EcType>) : EcType(getEnumId(types)) {
    companion object {
        private fun getEnumId(types: Set<EcType>) = "Enum(${types.joinToString()})"

        fun of(set: Set<EcType>): EcType {
            if (set.isEmpty()) return EcTypeNone
            else if (set.size == 1) return set.first()
            val flat = set.map { it.expand() }.flatten()
            return ALL_TYPES.getOrPut(getEnumId(set)) { EcTypeEnum(flat.toHashSet()) }
        }

        fun of(vararg vals: EcType) = of(vals.toHashSet())
    }

    override fun accepts(location: Loc, type: EcType) = type.expand().all { other -> types.any { it.accepts(location, other) } }

    /*
    override fun toString() = types.joinToString("&", "enum(", ")") { it.toString() }
    override fun accepts(location: Loc, type: EcType): Boolean {
        return if(type is EcTypeEnum)
            type.types.all { other -> types.any { it.accepts(location, other) } }
        else
            types.any { it.accepts(location, type) }
    }
     */

    override fun expand(): List<EcType> = types.toList()
}