package github.mrh0.eclang.ir.function.call

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder

class IRGlobalFunctionCall(location: Loc, val name: String, val id: String, val args: IRArguments) : IR(location) {
    override fun toString(): String {
        return "$name($args)"
    }

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put(id)
        sb.put('(')
        args.toC(sb, c)
        sb.put(')')
    }
}