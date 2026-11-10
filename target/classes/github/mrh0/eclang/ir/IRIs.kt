package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Type

/** True when [expr] (of type [from]) currently holds a [target]: `x is Int`, `x == null`, `p is @Cat`. */
class IRIs(location: Loc, val expr: IIR, val from: Type, val target: Type, val negated: Boolean = false) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        val check = sb.types.isCheck(from, target, sb.render { expr.toC(it, c) })
        sb.put(if (negated) "!$check" else check)
    }

    override fun toString() = "Is($expr: $from is $target)"
}
