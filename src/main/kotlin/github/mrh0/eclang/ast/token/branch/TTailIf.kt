package github.mrh0.eclang.ast.token.branch

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.NonValueTok
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.op.requireBool
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.Narrowing
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.ir.IRPass
import github.mrh0.eclang.ir.branch.IRTailIf
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** `statement if condition`. After `ret 0 if x == null`, x is known not to be null. */
class TTailIf(location: Loc, private val body: ITok, private val condition: ITok?) : NonValueTok(location) {
    val isConditional get() = condition != null

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        if (condition == null) return body.process(cd, hint)
        when (Narrowing.constant(cd, condition)) {
            false -> return Types.None to IRPass(location, null)
            true -> {
                // Always taken in this copy of the function: if it leaves, the rest is skipped, not an error.
                cd.ctx().push()
                val ir = body.process(cd, hint)
                val leaves = cd.ctx().hasReturned()
                cd.ctx().pop()
                if (leaves) cd.ctx().setReturned(statically = true)
                return ir
            }
            null -> {}
        }
        val conditionIR = requireBool(cd, location, "The `if` condition", condition.process(cd, Types.None))
        cd.ctx().push()
        Narrowing.apply(cd, Narrowing.of(cd, condition, true))
        // Anything the body hoists (a `try`) must only run when the condition holds, so it goes inside the if.
        val (hoisted, bodyIR) = cd.collectHoisted { body.process(cd, hint).second }
        val leaves = cd.ctx().hasReturned()
        cd.ctx().pop()
        if (leaves) Narrowing.apply(cd, Narrowing.of(cd, condition, false))
        return Types.None to IRTailIf(location, IRBlock(location, hoisted + bodyIR, listOf()), conditionIR)
    }

    override fun toString(): String {
        return "TTailIf($body, $condition)"
    }
}
