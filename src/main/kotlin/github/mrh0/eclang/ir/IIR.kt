package github.mrh0.eclang.ir

import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

interface IIR {
    fun toC(sb: CSourceBuilder, c: Context)
    fun deterministic() = false
}