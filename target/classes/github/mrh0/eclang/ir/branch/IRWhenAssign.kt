package github.mrh0.eclang.ir.branch

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder

/** `temp = value;` in an arm of a `when` used as a value. [value] is set once every arm is compiled and the `when`'s type is known. */
class IRWhenAssign(location: Loc, private val temp: String) : IR(location) {
    lateinit var value: IIR

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.putStatement("$temp = ")
        value.toC(sb, c)
        sb.endStatement()
    }

    override fun toString() = "$temp = $value"
}
