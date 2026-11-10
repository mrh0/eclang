package github.mrh0.eclang.ast.token.op.arithmetic

import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.op.arithmetic
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.arithmetic.sub.IRSub
import github.mrh0.ect2.Type

class TSub(location: Loc, val left: ITok, val right: ITok) : Tok(location) {
    override fun pointerFlow(): PointerFlow = PointerFlow.Nothing // a number or Bool

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> =
        arithmetic(cd, location, "-", left, right, { a, b -> Math.subtractExact(a, b) }, { a, b -> a - b }, checked = "-") { l, r -> IRSub(location, l, r) }

    override fun toString(): String {
        return "TSub($left, $right)"
    }
}
