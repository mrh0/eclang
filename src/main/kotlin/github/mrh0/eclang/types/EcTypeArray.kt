package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.numbers.unsigned.EcTypeSize

data class EcTypeArray(val arg: EcType) : EcType("Array") {
    override fun toString() = "$arg[]"

    override fun accepts(location: Loc, type: EcType): Boolean {
        if(type !is EcTypeArray) return false
        return arg.accepts(location, type.arg)
    }

    override fun getProperty(location: Loc, name: String): EcType? {
        return when (name) {
            "len" -> EcTypeSize
            "data" -> EcTypeCString
            else -> null
        }
    }
}