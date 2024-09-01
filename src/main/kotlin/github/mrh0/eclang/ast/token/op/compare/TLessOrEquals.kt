package github.mrh0.eclang.ast.token.op.compare

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.error.EcOpTypeError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.compare.less.IRLessOrEqualsStringString
import github.mrh0.eclang.ir.compare.less.IRLessOrEquals
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeBool
import github.mrh0.eclang.types.EcTypeString
import github.mrh0.eclang.types.numbers.EcTypeNumber

class TLessOrEquals(location: Loc, private val left: ITok, private val right: ITok) : Tok(location) {
    override fun toString(): String {
        return "($left < $right)"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val l = left.process(cd, hint);
        val r = right.process(cd, hint);
        return when {
            l.first is EcTypeNumber && r.first is EcTypeNumber -> Pair(EcTypeBool, IRLessOrEquals(location, l.second, r.second))
            l.first is EcTypeString && r.first is EcTypeString -> Pair(EcTypeBool, IRLessOrEqualsStringString(location, l.second, r.second))
            else -> throw EcOpTypeError(location, "==", l.first, r.first)
        }
    }
}
