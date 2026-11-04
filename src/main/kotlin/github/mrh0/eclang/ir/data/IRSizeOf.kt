package github.mrh0.eclang.ir.data

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

/** `sizeof(T)`, `alignof(T)` ([keyword]), or `offsetof(T, field)` when [field] is given. */
class IRSizeOf(location: Loc, val expr: IIR, private val keyword: String = "sizeof", private val field: String? = null) : IR(location) {
    override fun toString() = "$keyword($expr${field?.let { ", $it" } ?: ""})"

    override fun isConstant() = expr.isConstant()

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put("$keyword(")
        expr.toC(sb, c)
        field?.let { sb.put(", $it") }
        sb.put(')')
    }
}
