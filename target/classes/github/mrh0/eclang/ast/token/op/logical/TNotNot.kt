package github.mrh0.eclang.ast.token.op.logical

import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.error.EcOpTypeError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.compare.equals.IREquals
import github.mrh0.eclang.ir.data.IRBool
import github.mrh0.eclang.ir.data.IRInt
import github.mrh0.eclang.ir.logical.notnot.IRNotNotInt
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeBool
import github.mrh0.eclang.types.EcTypeNull
import github.mrh0.eclang.types.numbers.signed.EcTypeInt

class TNotNot(location: Loc, val expr: ITok) : Tok(location) {
    override fun toString(): String {
        return "TNotNot($expr)"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val e = expr.process(cd, hint);
        return when {
            e.first.accepts(location, EcTypeBool) -> EcTypeBool to e.second
            e.first.accepts(location, EcTypeInt) -> EcTypeBool to IRNotNotInt(location, e.second)
            e.first is EcTypeNull -> EcTypeBool to IRBool(location, false)
            e.first.accepts(location, EcTypeNull) -> EcTypeBool to IREquals(location, e.second, IRInt(location, 0))
            else -> throw EcOpTypeError(location, "!!", e.first)
        }
    }
}
