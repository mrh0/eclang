package github.mrh0.eclang.ast.token.op.compare

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.compare.not_equals.IRNotEqualsFallback
import github.mrh0.eclang.ir.compare.not_equals.IRNotEqualsIntInt
import github.mrh0.eclang.types.numbers.EcTypeInt
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeBool

class TNotEquals(location: Loc, private val left: ITok, private val right: ITok) : Tok(location) {
    override fun toString(): String {
        return "($left != $right)"
    }

    override fun process(cd: CompileData): Pair<EcType, IIR> {
        val l = left.process(cd);
        val r = right.process(cd);
        return when {
            l.first is EcTypeInt && r.first is EcTypeInt -> Pair(EcTypeBool, IRNotEqualsIntInt(location, l.second, r.second))
            else -> Pair(EcTypeBool, IRNotEqualsFallback(location, l.second, r.second))
        }
    }
}
