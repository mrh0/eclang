package github.mrh0.eclang.ast.token.op.arithmetic

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.error.EcOpTypeError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.arithmetic.negate.IRNegateInt
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.numbers.EcTypeInt

class TNegate(location: Loc, val expr: ITok) : Tok(location) {
    override fun toString(): String {
        return "TNegate($expr)"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val e = expr.process(cd, hint);
        return when (e.first) {
            is EcTypeInt -> Pair(EcTypeInt, IRNegateInt(location, e.second))
            else -> throw EcOpTypeError(location, "-", e.first)
        }
    }
}
