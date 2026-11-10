package github.mrh0.eclang.ast.token.statement

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.NonValueTok
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.ast.token.data.TNull
import github.mrh0.eclang.ast.token.op.compare.TEquals
import github.mrh0.eclang.ast.unnested
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.Narrowing
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.ir.IRIs
import github.mrh0.eclang.ir.IRRaw
import github.mrh0.eclang.ir.IRTempDefine
import github.mrh0.eclang.ir.IRTryCatch
import github.mrh0.eclang.ir.branch.IRStatementIf
import github.mrh0.ect2.Result
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types
import github.mrh0.ect2.Union

/**
 * `a ?? action` as a statement: runs [action], a call or a jump, when a is null or a call that failed. Nothing keeps a's
 * value, so unlike `val x = a ?? b` it also works on a call that returns nothing. After an [action] that leaves, a variable
 * a is known not to be null.
 */
class TStatementNullish(location: Loc, private val left: ITok, private val action: ITok) : NonValueTok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val (lt, lir) = left.process(cd, Types.None)
        if (lt is Result) return Types.None to IRTryCatch(location, cd.temp(), lt, lir, cd.temp(), action(cd))
        if (lt !is Union || !lt.isNullable) throw EcError(location, "A value of type $lt is never null, so `??` is not needed here.")
        val read = if (left.unnested() is TNamed) lir else {
            val temp = cd.temp()
            cd.hoist(location, IRTempDefine(location, temp, lt, lir))
            IRRaw(location, temp)
        }
        val body = action(cd)
        if (body.diverges) Narrowing.apply(cd, Narrowing.of(cd, TEquals(location, left.unnested(), TNull(location)), false))
        return Types.None to IRStatementIf(location, listOf(IRIs(location, read, lt, Types.Null)), listOf(body))
    }

    /** [action] in a scope of its own: the code after it does run. */
    private fun action(cd: CompileData): IRBlock {
        cd.ctx().push()
        val (hoisted, ir) = cd.collectHoisted { action.process(cd, Types.None).second }
        val leaves = cd.ctx().hasReturned()
        cd.ctx().pop()
        return IRBlock(location, hoisted + ir, listOf(), diverges = leaves)
    }

    override fun toString() = "TStatementNullish($left, $action)"
}
