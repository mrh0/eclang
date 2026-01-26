package github.mrh0.eclang.ast.token.op.compare

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.error.EcOpTypeError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.compare.greater.IRGreaterOrEquals
import github.mrh0.eclang.ir.compare.greater.IRGreaterOrEqualsStringString
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeBool
import github.mrh0.eclang.types.EcTypeCString
import github.mrh0.eclang.types.numbers.EcTypeNumber

class TGreaterOrEquals(location: Loc, private val left: ITok, private val right: ITok) : Tok(location) {
    override fun toString(): String {
        return "($left < $right)"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val l = left.process(cd, hint);
        val r = right.process(cd, hint);
        return when {
            l.first is EcTypeNumber && r.first is EcTypeNumber -> Pair(EcTypeBool, IRGreaterOrEquals(location, l.second, r.second))
            l.first is EcTypeCString && r.first is EcTypeCString -> Pair(EcTypeBool, IRGreaterOrEqualsStringString(location, l.second, r.second))
            else -> throw EcOpTypeError(location, "==", l.first, r.first)
        }
    }
}
