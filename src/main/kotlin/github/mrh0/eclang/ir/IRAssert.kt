package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.eclang.util.StringLiteral

/** `if (!(condition)) __ec_panic("file:line", "message");` */
class IRAssert(location: Loc, private val condition: IIR, private val message: StringLiteral) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.putStatement("if (!(")
        condition.toC(sb, c)
        sb.put(")) __ec_panic(${IRConvert.where(location)}, \"${message.toC()}\")")
        sb.endStatement()
    }

    override fun toString() = "Assert($condition)"
}
