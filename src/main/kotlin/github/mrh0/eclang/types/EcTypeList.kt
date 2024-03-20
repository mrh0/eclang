package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc

class EcTypeList(val arg: EcTypeAny) : EcTypeAny("Gs", "List") {
    override fun toString() = "List<$arg>"

    override fun accepts(location: Loc, type: EcTypeAny): Boolean {
        if(type !is EcTypeList) return false
        if(!arg.accepts(location, type.arg)) return false
        return true
    }
}