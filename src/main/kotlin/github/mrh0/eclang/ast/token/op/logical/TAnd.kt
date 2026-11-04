package github.mrh0.eclang.ast.token.op.logical

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.op.requireBool
import github.mrh0.eclang.ast.token.op.shortCircuitRight
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.logical.and.IRAnd
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

class TAnd(location: Loc, val left: ITok, val right: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val l = requireBool(cd, location, "The left side of `and`", left.process(cd, Types.None))
        val r = requireBool(cd, location, "The right side of `and`", shortCircuitRight(cd, left, right, true))
        return Types.Bool to IRAnd(location, l, r)
    }

    override fun toString(): String {
        return "TAnd($left, $right)"
    }
}
