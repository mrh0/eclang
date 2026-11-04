package github.mrh0.eclang.ast.token.op.arithmetic

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.op.arithmetic
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.arithmetic.mul.IRMul
import github.mrh0.ect2.Type

class TMul(location: Loc, val left: ITok, val right: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> =
        arithmetic(cd, location, "*", left, right, { a, b -> Math.multiplyExact(a, b) }, { a, b -> a * b }, checked = "*") { l, r -> IRMul(location, l, r) }

    override fun toString(): String {
        return "TMul($left, $right)"
    }
}
