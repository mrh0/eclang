package github.mrh0.eclang.ast.token.op.logical

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.error.EcOpTypeError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.logical.not.IRNotBool
import github.mrh0.eclang.ir.logical.not.IRNotInt
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeBool
import github.mrh0.eclang.types.numbers.EcTypeInt

class TNot(location: Loc, val expr: ITok) : Tok(location) {
    override fun toString(): String {
        return "TNot($expr)"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val e = expr.process(cd, hint);
        return when (e.first) {
            is EcTypeBool -> Pair(EcTypeBool, IRNotBool(location, e.second))
            is EcTypeInt -> Pair(EcTypeBool, IRNotInt(location, e.second))
            else -> throw EcOpTypeError(location, "!", e.first)
        }
    }
}
