package github.mrh0.eclang.ast.token.op.arithmetic

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.error.EcOpTypeError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.arithmetic.add.IRAdd
import github.mrh0.eclang.ir.arithmetic.mul.IRMul
import github.mrh0.eclang.ir.arithmetic.pow.IRPowDoubleDouble
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.numbers.EcTypeFloat
import github.mrh0.eclang.types.numbers.EcTypeInt
import github.mrh0.eclang.types.numbers.EcTypeNumber

class TPow(location: Loc, val left: ITok, val right: ITok) : Tok(location) {
    override fun toString(): String {
        return "($left ** $right)"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val l = left.process(cd, hint);
        val r = right.process(cd, hint);
        return when {
            l.first is EcTypeInt || r.first is EcTypeInt -> Pair(EcTypeFloat, IRPowDoubleDouble(location, l.second, r.second))
            l.first is EcTypeNumber || r.first is EcTypeNumber -> Pair(l.first, IRPowDoubleDouble(location, l.second, r.second))
            else -> throw EcOpTypeError(location, "**", l.first, r.first)
        }
    }
}
