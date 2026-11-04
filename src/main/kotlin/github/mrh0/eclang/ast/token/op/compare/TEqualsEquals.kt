package github.mrh0.eclang.ast.token.op.compare

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.compare.equals.IREquals
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** `a === b`: identity, the plain C comparison (two CStrings are identical only if they are the same pointer). */
class TEqualsEquals(location: Loc, private val left: ITok, private val right: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val l = cd.usable(location, left.process(cd, Types.None), "`===`")
        val r = cd.usable(location, right.process(cd, Types.None), "`===`")
        return Types.Bool to IREquals(location, l.second, r.second)
    }

    override fun toString(): String {
        return "TEqualsEquals($left, $right)"
    }
}
