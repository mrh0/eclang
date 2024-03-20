package github.mrh0.eclang.ir.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.types.EcTypeAny

class IRFunc(location: Loc, private val block: IRBlock, val name: String, val args: Array<Pair<String, EcTypeAny>>, val returnType: EcTypeAny) : IR(location) {
    override fun toString(): String {
        return "fn($name, $block)"
    }
}