package github.mrh0.eclang.ast.token.op.logical

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.op.operands
import github.mrh0.eclang.ast.token.op.requireBool
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.logical.xor.IRXOr
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

class TXOr(location: Loc, val left: ITok, val right: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> = operands(cd, location, left, right) { a, b ->
        val l = requireBool(cd, location, "The left side of `xor`", a)
        val r = requireBool(cd, location, "The right side of `xor`", b)
        Types.Bool to IRXOr(location, l, r)
    }

    override fun toString(): String {
        return "TXOr($left, $right)"
    }
}
