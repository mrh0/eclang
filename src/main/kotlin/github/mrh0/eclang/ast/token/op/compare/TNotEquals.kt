package github.mrh0.eclang.ast.token.op.compare

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.error.EcOpTypeError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.compare.not_equals.IRNotEqualsFallback
import github.mrh0.eclang.ir.compare.not_equals.IRNotEquals
import github.mrh0.eclang.ir.compare.not_equals.IRNotEqualsStringString
import github.mrh0.eclang.types.*
import github.mrh0.eclang.types.numbers.EcTypeNumber

class TNotEquals(location: Loc, private val left: ITok, private val right: ITok) : Tok(location) {
    override fun toString(): String {
        return "($left != $right)"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val l = left.process(cd, hint);
        val r = right.process(cd, hint);
        return when {
            l.first is EcTypeNumber && r.first is EcTypeNumber -> Pair(EcTypeBool, IRNotEquals(location, l.second, r.second))
            l.first is EcTypeAtom && r.first is EcTypeAtom -> Pair(EcTypeBool, IRNotEquals(location, l.second, r.second))
            l.first is EcTypeAtomInstance && r.first is EcTypeAtomInstance -> Pair(EcTypeBool, IRNotEquals(location, l.second, r.second))
            l.first is EcTypeCString && r.first is EcTypeCString -> Pair(EcTypeBool, IRNotEqualsStringString(location, l.second, r.second))
            l == r -> Pair(EcTypeBool, IRNotEqualsFallback(location, l.second, r.second))
            else -> throw EcOpTypeError(location, "!=", l.first, r.first)
        }
    }
}
