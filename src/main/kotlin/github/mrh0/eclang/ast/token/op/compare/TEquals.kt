package github.mrh0.eclang.ast.token.op.compare

import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.op.CompareKind
import github.mrh0.eclang.ast.token.op.comparison
import github.mrh0.eclang.ast.token.op.nullComparison
import github.mrh0.eclang.ast.token.op.operands
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRRawCall
import github.mrh0.eclang.ir.compare.equals.IREquals
import github.mrh0.eclang.ir.compare.equals.IREqualsStringString
import github.mrh0.eclang.ir.logical.not.IRNotBool
import github.mrh0.eclang.ir.compare.equals.IREqualsRecord
import github.mrh0.eclang.ir.compare.equals.IREqualsText
import github.mrh0.eclang.ast.token.op.isText
import github.mrh0.ect2.FixedArray
import github.mrh0.eclang.ast.token.op.Ops
import github.mrh0.ect2.Tuple
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** `a == b`: compares values; CStrings by content (`===` compares identity). `x == null` checks for null. */
class TEquals(location: Loc, val left: ITok, val right: ITok) : Tok(location) {
    override fun pointerFlow(): PointerFlow = PointerFlow.Nothing // a number or Bool

    // The hint is so that `t == (1, 2)` works on a `(Long, Long)`.
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> = operands(cd, location, left, right, { it as? Tuple ?: Types.None }) { l, r -> compare(cd, l, r) }

    private fun compare(cd: CompileData, l: Pair<Type, IIR>, r: Pair<Type, IIR>): Pair<Type, IIR> {
        nullComparison(cd, location, l, r, negated = false)?.let { return it }
        val (kind, a, b) = comparison(cd, location, "==", l, r, equality = true)
        return Types.Bool to when (kind) {
            CompareKind.CSTRING -> IREqualsStringString(location, a, b)
            CompareKind.STRING -> IRRawCall(location, "__ec_str_eq", listOf(a, b)).let { if (false) IRNotBool(location, it) else it }
            CompareKind.RECORD -> IREqualsRecord(location, a, b, Ops.base(l.first), negated = false)
            CompareKind.TEXT -> IREqualsText(location, a, b, Ops.base(if (isText(l.first)) l.first else r.first) as FixedArray, negated = false)
            else -> IREquals(location, a, b)
        }
    }

    override fun toString(): String {
        return "TEquals($left, $right)"
    }
}
