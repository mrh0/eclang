package github.mrh0.eclang.ast.token.op.arithmetic

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.op.arithmetic
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.arithmetic.div.IRDiv
import github.mrh0.ect2.Type

/** `a / b`. Two whole numbers divide to a whole number, rounding towards zero (like C). */
class TDiv(location: Loc, val left: ITok, val right: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> =
        arithmetic(cd, location, "/", left, right,
            { a, b -> if (b == 0L) throw ArithmeticException("division by zero") else a / b },
            { a, b -> if (b == 0.0) throw ArithmeticException("division by zero") else a / b },
        ) { l, r -> IRDiv(location, l, r) }

    override fun toString(): String {
        return "TDiv($left, $right)"
    }
}
