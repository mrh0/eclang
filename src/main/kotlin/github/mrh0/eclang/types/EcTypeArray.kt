package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc

class EcTypeArray(val arg: EcType) : EcType("List") {
    override fun toString() = "$arg[]"

    override fun accepts(location: Loc, type: EcType): Boolean {
        if(type !is EcTypeArray) return false
        return arg.accepts(location, type.arg)
    }
}