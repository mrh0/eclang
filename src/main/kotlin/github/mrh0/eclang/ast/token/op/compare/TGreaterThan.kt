package github.mrh0.eclang.ast.token.op.compare

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.op.CompareKind
import github.mrh0.eclang.ast.token.op.comparison
import github.mrh0.eclang.ast.token.op.operands
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.compare.greater.IRGreaterThan
import github.mrh0.eclang.ir.compare.greater.IRGreaterThanStringString
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** `a > b` on numbers, or on CStrings (alphabetical order). */
class TGreaterThan(location: Loc, private val left: ITok, private val right: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> = operands(cd, location, left, right) { l, r ->
        val (kind, a, b) = comparison(cd, location, ">", l, r, equality = false)
        Types.Bool to if (kind == CompareKind.CSTRING) IRGreaterThanStringString(location, a, b) else IRGreaterThan(location, a, b)
    }

    override fun toString(): String {
        return "TGreaterThan($left, $right)"
    }
}
