package github.mrh0.eclang.ir.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.types.EcTypeAny
import github.mrh0.eclang.values.GsBase
import github.mrh0.eclang.context.Context

class IRFunc(location: Loc, private val block: IRBlock, val name: String, val args: Array<Pair<String, EcTypeAny>>, val returnType: EcTypeAny) : IR(location) {
    override fun toString(): String {
        return "fn($name, $block)"
    }

    override fun evaluate(vm: VM, c: Context): GsBase {
        val result = block.evaluate(vm, vm.getContext(name)!!) // Should never be null
        c.captureReturn()
        return result
    }
}