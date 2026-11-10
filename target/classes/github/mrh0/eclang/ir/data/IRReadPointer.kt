package github.mrh0.eclang.ir.data

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

/** `*p`. With [parenthesized], `(*p)`: a `mut` parameter, which the Visitor turned into `@p` where the code says `p`. */
class IRReadPointer(location: Loc, val expr: IIR, val parenthesized: Boolean = false) : IR(location) {
    override fun toString() = "(@ $expr)"

    override fun toC(sb: CSourceBuilder, c: Context) {
        if (parenthesized) sb.put('(')
        sb.put('*')
        expr.toC(sb, c)
        if (parenthesized) sb.put(')')
    }
}
