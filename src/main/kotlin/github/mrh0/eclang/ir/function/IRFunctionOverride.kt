package github.mrh0.eclang.ir.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.output.BlockScope
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.eclang.types.EcType

class IRFunctionOverride(location: Loc, private val block: IRBlock, val name: String, val params: IRParameters, val returnType: EcType) : IR(location) {
    override fun toString(): String {
        return "fn($name, $params, $returnType, $block)"
    }

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.putStatement()
        IRType(location, returnType).toC(sb, c)
        sb.put(' ')
        sb.put(name)
        sb.put(" (")
        params.toC(sb, c)
        sb.put(") ")
        sb.pushScope(BlockScope())
        block.toC(sb, c)
        sb.popScope()
    }
}