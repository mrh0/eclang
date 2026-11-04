package github.mrh0.eclang.ir.function.call

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.ir.IRNamed
import github.mrh0.eclang.ir.IRNamedConstant
import github.mrh0.eclang.output.c.CSourceBuilder

/** A call of a function value (a C function pointer): `f(x)`, or `(p->callback)(x)`. */
class IRCallValue(location: Loc, private val callee: IIR, private val args: List<IIR>) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        val simple = callee is IRNamed || callee is IRNamedConstant
        if (!simple) sb.put('(')
        callee.toC(sb, c)
        if (!simple) sb.put(')')
        sb.put('(')
        args.forEachIndexed { i, it -> if (i != 0) sb.put(", "); it.toC(sb, c) }
        sb.put(')')
    }

    override fun toString() = "$callee($args)"
}
