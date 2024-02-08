package github.mrh0.eclang.ast.token.op.logical

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.error.EcOpTypeError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.logical.notnot.IRNotNotInt
import github.mrh0.eclang.types.EcTypeAny
import github.mrh0.eclang.types.EcTypeBool
import github.mrh0.eclang.types.numbers.EcTypeInt

class TNotNot(location: Loc, val expr: ITok) : Tok(location) {
    override fun toString(): String {
        return "TNotNot($expr)"
    }

    override fun process(cd: CompileData): Pair<EcTypeAny, IIR> {
        val e = expr.process(cd);
        return when (e.first) {
            is EcTypeBool -> Pair(EcTypeBool, e.second)
            is EcTypeInt -> Pair(EcTypeBool, IRNotNotInt(location, e.second))
            else -> throw EcOpTypeError(location, "!!", e.first)
        }
    }
}
