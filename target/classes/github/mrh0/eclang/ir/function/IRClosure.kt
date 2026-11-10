package github.mrh0.eclang.ir.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder

/**
 * A function value whose C function is [id] (see [IRFunctionOverride.ClosureC]): its static closure, `(&__ec_value_id)`,
 * or, with [captured] (the starting values of the variables a lambda uses), a new one, `__ec_new_id(limit, &count)`.
 */
class IRClosure(location: Loc, private val id: String, private val captured: List<IIR>) : IR(location) {
    override fun isConstant() = captured.isEmpty()

    override fun toC(sb: CSourceBuilder, c: Context) {
        if (captured.isEmpty()) {
            sb.put("(&${IRFunctionOverride.valueName(id)})")
            return
        }
        sb.put("${IRFunctionOverride.newName(id)}(")
        captured.forEachIndexed { i, it -> if (i != 0) sb.put(", "); it.toC(sb, c) }
        sb.put(")")
    }

    override fun toString() = "IRClosure($id, $captured)"
}
