package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc

class EcTypeUnion private constructor(val types: Set<EcType>) : EcType(getUnionId(types)) {
    companion object {
        private fun getUnionId(types: Set<EcType>) = "Union(${types.joinToString()})"
        // fun of(type: EcType, right: EcType): EcType = ALL_TYPES.getOrPut(getUnionId(type, right)) { EcTypeUnion(type, right) }

        fun of(set: Set<EcType>): EcType {
            if (set.isEmpty()) return EcTypeNone
            else if (set.size == 1) return set.first()
            val flat = set.map { it.expand() }.flatten()
            return ALL_TYPES.getOrPut(getUnionId(set)) { EcTypeUnion(flat.toHashSet()) }
        }

        fun of(vararg vals: EcType) = of(vals.toHashSet())
    }

    fun notNullableCopy() = of(types.filter { it != EcTypeNull }.toHashSet())

    override fun accepts(location: Loc, type: EcType) = types.any { it.accepts(location, type) }

    /*
    override fun union(other: EcType): EcType {
        if (other.uid < this.uid) {
            return of(other, this)
        }

        var current = this
        while (current.right is EcTypeUnion && (current.right).uid < other.uid) {
            current = current.right as EcTypeUnion
        }

        val next = current.right
        current.right = of(other, next)
        return this
    }

    init {
        if (type is EcTypeUnion) throw Error("BUG: root type cannot be union, $type, $right")
    }
    */

    override fun expand(): List<EcType> = types.toList()

    override fun canBeInstantiated() = false
}