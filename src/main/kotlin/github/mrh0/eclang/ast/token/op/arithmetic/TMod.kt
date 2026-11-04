package github.mrh0.eclang.ast.token.op.arithmetic

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.op.arithmetic
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.arithmetic.div.IRMod
import github.mrh0.ect2.Type

class TMod(location: Loc, val left: ITok, val right: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> =
        arithmetic(cd, location, "%", left, right,
            { a, b -> if (b == 0L) throw ArithmeticException("division by zero") else a % b }, null, integerOnly = true,
            fractionHint = " For the remainder of Floats or Doubles, call `fmod(a, b)` (from core:lib.ec).",
        ) { l, r -> IRMod(location, l, r) }

    override fun toString(): String {
        return "TMod($left, $right)"
    }
}
