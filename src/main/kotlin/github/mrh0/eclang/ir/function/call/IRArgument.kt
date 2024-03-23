package github.mrh0.eclang.ir.function.call

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder

class IRArgument(location: Loc, val next: IIR) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        next.toC(sb, c)
    }
}