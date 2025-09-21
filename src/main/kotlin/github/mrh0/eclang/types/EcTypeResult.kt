package github.mrh0.eclang.types

data class EcTypeResult(val returnedType: EcType, val throwableType: EcType) : EcType("", EcTypeTuple.of(returnedType, throwableType)) {
    override fun isReferenceType() = true
}

// Convert to Boxed(Union(r, e))