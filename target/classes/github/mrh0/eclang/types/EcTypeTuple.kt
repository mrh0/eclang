package github.mrh0.eclang.types

open class EcTypeTuple private constructor(val types: Array<EcType>) : EcType("Tuple") {
    companion object {
        private fun getTupleId(types: Array<out EcType>) = "Tuple(${types.joinToString()})"

        fun of(types: Array<EcType>): EcType {
            if (types.isEmpty()) return EcTypeNone
            else if (types.size == 1) return types.first()
            return ALL_TYPES.getOrPut(getTupleId(types)) { EcTypeTuple(types) }
        }
    }

    override fun isReferenceType() = true
}