package github.mrh0.eclang.ast.token.op.compare

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.error.EcOpTypeError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.compare.equals.IREqualsFallback
import github.mrh0.eclang.ir.compare.equals.IREquals
import github.mrh0.eclang.ir.compare.equals.IREqualsStringString
import github.mrh0.eclang.types.*
import github.mrh0.eclang.types.numbers.EcTypeNumber

class TEqualsEquals(location: Loc, private val left: ITok, private val right: ITok) : Tok(location) {
    override fun toString(): String {
        return "($left === $right)"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val l = left.process(cd, hint);
        val r = right.process(cd, hint);
        return Pair(EcTypeBool, IREquals(location, l.second, r.second))
    }
}
