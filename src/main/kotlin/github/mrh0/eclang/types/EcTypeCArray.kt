package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc

data class EcTypeCArray(val arg: EcType) : EcType("CArray") {
    override fun toString() = "$arg[]c"

    override fun accepts(location: Loc, type: EcType): Boolean {
        if(type !is EcTypeCArray) return false
        return arg.accepts(location, type.arg)
    }

    override fun isReferenceType(): Boolean = true
}