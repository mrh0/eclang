package github.mrh0.eclang.ast.token.op.compare

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.op.CompareKind
import github.mrh0.eclang.ast.token.op.comparison
import github.mrh0.eclang.ast.token.op.nullComparison
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRRawCall
import github.mrh0.eclang.ir.compare.not_equals.IRNotEquals
import github.mrh0.eclang.ir.compare.not_equals.IRNotEqualsStringString
import github.mrh0.eclang.ir.logical.not.IRNotBool
import github.mrh0.eclang.ir.compare.equals.IREqualsRecord
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** `a != b`: compares values; CStrings by content (`===` compares identity). `x != null` checks for null. */
class TNotEquals(location: Loc, val left: ITok, val right: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val l = left.process(cd, Types.None)
        val r = right.process(cd, Types.None)
        nullComparison(cd, location, l, r, negated = true)?.let { return it }
        val (kind, a, b) = comparison(cd, location, "!=", l, r, equality = true)
        return Types.Bool to when (kind) {
            CompareKind.CSTRING -> IRNotEqualsStringString(location, a, b)
            CompareKind.STRING -> IRRawCall(location, "__ec_str_eq", listOf(a, b)).let { if (true) IRNotBool(location, it) else it }
            CompareKind.RECORD -> IREqualsRecord(location, a, b, l.first as github.mrh0.ect2.Record, negated = true)
            else -> IRNotEquals(location, a, b)
        }
    }

    override fun toString(): String {
        return "TNotEquals($left, $right)"
    }
}
