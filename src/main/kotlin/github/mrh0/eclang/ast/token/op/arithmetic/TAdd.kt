package github.mrh0.eclang.ast.token.op.arithmetic

import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.op.arithmetic
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.arithmetic.add.IRAdd
import github.mrh0.ect2.Type

class TAdd(location: Loc, val left: ITok, val right: ITok) : Tok(location) {
    override fun pointerFlow(): PointerFlow = PointerFlow.Nothing // a number or Bool

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> =
        arithmetic(cd, location, "+", left, right, { a, b -> Math.addExact(a, b) }, { a, b -> a + b }, checked = "+") { l, r -> IRAdd(location, l, r) }

    override fun toString(): String {
        return "TAdd($left, $right)"
    }
}
