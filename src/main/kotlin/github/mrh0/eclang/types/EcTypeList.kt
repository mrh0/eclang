package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc

class EcTypeList(val arg: EcType) : EcType("Ec", "List") {
    override fun toString() = "List<$arg>"

    override fun accepts(location: Loc, type: EcType): Boolean {
        if(type !is EcTypeList) return false
        if(!arg.accepts(location, type.arg)) return false
        return true
    }
}