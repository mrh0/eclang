package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc

class EcTypeUnion private constructor(val types: Set<EcType>) : EcType(getUnionId(types)) {
    companion object {
        private fun getUnionId(types: Set<EcType>) = "Union(${types.joinToString()})"

        fun of(set: Set<EcType>): EcType {
            if (set.isEmpty()) return EcTypeNone
            else if (set.size == 1) return set.first()
            val flat = set.map { it.expand() }.flatten()
            return ALL_TYPES.getOrPut(getUnionId(set)) { EcTypeUnion(flat.toHashSet()) }
        }

        fun of(vararg vals: EcType) = of(vals.toHashSet())
    }

    fun notNullableCopy() = of(types.filter { it != EcTypeNull }.toHashSet())

    override fun accepts(location: Loc, type: EcType) = type.expand().all { other -> types.any { it.accepts(location, other) } }

    override fun expand(): List<EcType> = types.toList()

    override fun canBeInstantiated() = false
}