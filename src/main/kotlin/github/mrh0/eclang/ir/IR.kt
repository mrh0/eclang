package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

abstract class IR(val location: Loc) : IIR {
    override fun toC(sb: CSourceBuilder, c: Context) {
        TODO("Not yet implemented")
    }
}