package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.numbers.signed.EcTypeSignedInteger
import github.mrh0.eclang.types.numbers.unsigned.EcTypeSize
import github.mrh0.eclang.types.numbers.unsigned.EcTypeUnsignedInteger

class EcTypeArray private constructor(val arg: EcType) : EcType(getArrayId(arg)) {
    companion object {
        private fun getArrayId(wrapped: EcType) = "Array($wrapped)"

        fun of(wrapped: EcType): EcType = ALL_TYPES.getOrPut(getArrayId(wrapped)) { EcTypeArray(wrapped) }
    }

    override fun getProperty(location: Loc, name: String): EcType {
        return when (name) {
            "len" -> EcTypeSize
            "data" -> EcTypeCString
            else -> super.getProperty(location, name)
        }
    }

    override fun accessor(location: Loc, indexType: EcType): EcType {
        return when (indexType) {
            is EcTypeSignedInteger, is EcTypeUnsignedInteger -> arg // TODO: Rework this check
            else -> super.accessor(location, indexType)
        }
    }

    override fun isReferenceType(): Boolean = true
}