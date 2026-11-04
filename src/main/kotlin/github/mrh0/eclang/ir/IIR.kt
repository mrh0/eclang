package github.mrh0.eclang.ir

import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

interface IIR {
    fun toC(sb: CSourceBuilder, c: Context)
    fun toCPre(sb: CSourceBuilder, c: Context)
    fun toCPost(sb: CSourceBuilder, c: Context)
    /** True for a C constant expression, which can initialize a global (`static int x = 1 << 2;`). */
    fun isConstant() = false
}