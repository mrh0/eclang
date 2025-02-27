package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.numbers.signed.EcTypeSignedInteger
import github.mrh0.eclang.types.numbers.unsigned.EcTypeSize
import github.mrh0.eclang.types.numbers.unsigned.EcTypeUnsignedInteger

data class EcTypeArray(val arg: EcType) : EcType("Array") {
    override fun toString() = "$arg[]"

    override fun accepts(location: Loc, type: EcType): Boolean {
        if(type !is EcTypeArray) return false
        return arg.accepts(location, type.arg)
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
            is EcTypeSignedInteger, is EcTypeUnsignedInteger -> arg
            else -> super.accessor(location, indexType)
        }
    }

    override fun isReferenceType(): Boolean = true
}