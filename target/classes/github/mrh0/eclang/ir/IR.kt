package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

abstract class IR(val location: Loc) : IIR {
    override fun toC(sb: CSourceBuilder, c: Context) {
        TODO("${this.javaClass.simpleName}.toC not yet implemented")
    }

    override fun toCPre(sb: CSourceBuilder, c: Context) {}

    override fun toCPost(sb: CSourceBuilder, c: Context) {}
}