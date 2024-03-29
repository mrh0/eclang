package github.mrh0.eclang.ir.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.output.BlockScope
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.eclang.types.EcType

class IRFunctionOverrides(location: Loc, val name: String, val irs: List<IIR>) : IR(location) {
    override fun toString(): String {
        return "fn($name)"
    }

    override fun toC(sb: CSourceBuilder, c: Context) {
        irs.map { it.toC(sb, c) }
    }
}