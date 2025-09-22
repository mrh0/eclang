package github.mrh0.eclang.types

class EcTypeResult private constructor(val returnedType: EcType, val throwableType: EcType) : EcType(getResultId(returnedType, throwableType)) {
    companion object {
        fun getResultId(returnedType: EcType, throwableType: EcType) = "Result($returnedType,$throwableType)"

        fun of(rt: EcType, et: EcType) = ALL_TYPES.getOrPut(getResultId(rt, et)) { EcTypeResult(rt, et) }
    }

    override fun isReferenceType() = true
}
