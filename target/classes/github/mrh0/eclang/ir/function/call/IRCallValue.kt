package github.mrh0.eclang.ir.function.call

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.ir.IRNamed
import github.mrh0.eclang.ir.IRNamedConstant
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.FunctionType

/**
 * A call of a function value of type [type]. A C function pointer is called as it is: `f(x)`, or `(p->callback)(x)`.
 * An eclang function value is a closure, given to its C function first: `f->call(f, x)`, or, when the closure isn't a
 * plain name that can be read twice, `__ec_call_M(make(), x)` (see [github.mrh0.eclang.output.c.CTypes.closureCall]).
 */
class IRCallValue(location: Loc, private val callee: IIR, private val args: List<IIR>, private val type: FunctionType) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        val simple = callee is IRNamed || callee is IRNamedConstant
        when {
            type.isC -> {
                if (!simple) sb.put('(')
                callee.toC(sb, c)
                if (!simple) sb.put(')')
                sb.put('(')
            }
            simple -> {
                callee.toC(sb, c)
                sb.put("->call(")
                callee.toC(sb, c)
                if (args.isNotEmpty()) sb.put(", ")
            }
            else -> {
                sb.put(sb.types.closureCall(type))
                sb.put('(')
                callee.toC(sb, c)
                if (args.isNotEmpty()) sb.put(", ")
            }
        }
        args.forEachIndexed { i, it -> if (i != 0) sb.put(", "); it.toC(sb, c) }
        sb.put(')')
    }

    override fun toString() = "$callee($args)"
}
