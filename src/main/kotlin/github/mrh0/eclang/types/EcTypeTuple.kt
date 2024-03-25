package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc

class EcTypeTuple(val args: Array<EcType>) : EcType("Ec", "Tuple") {
    override fun toString() = args.joinToString("&", "(", ")") { it.toString() }

    override fun accepts(location: Loc, type: EcType): Boolean {
        if(type !is EcTypeTuple) return false
        return args.zip(type.args).all { it.first.accepts(location, it.second) }
    }
}