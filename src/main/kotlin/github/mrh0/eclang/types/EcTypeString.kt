package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.numbers.unsigned.EcTypeSize
import github.mrh0.eclang.types.numbers.unsigned.EcTypeULong

object EcTypeString : EcType("String") {
    override fun accepts(location: Loc, type: EcType): Boolean = type == this

    override fun isReferenceType() = true

    override fun getProperty(location: Loc, name: String): EcType? {
        return when (name) {
            "len" -> EcTypeSize
            "data" -> EcTypeCString
            else -> null
        }
    }
}