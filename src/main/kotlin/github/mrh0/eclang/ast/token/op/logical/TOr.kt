package github.mrh0.eclang.ast.token.op.logical

import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.op.requireBool
import github.mrh0.eclang.ast.token.op.shortCircuitRight
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.logical.or.IROr
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

class TOr(location: Loc, val left: ITok, val right: ITok) : Tok(location) {
    override fun pointerFlow(): PointerFlow = PointerFlow.Nothing // a number or Bool

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val l = requireBool(cd, location, "The left side of `or`", left.process(cd, Types.None))
        val r = requireBool(cd, location, "The right side of `or`", shortCircuitRight(cd, left, right, false))
        return Types.Bool to IROr(location, l, r)
    }

    override fun toString(): String {
        return "TOr($left, $right)"
    }
}
