package github.mrh0.eclang.ast.token.loop

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.TExprNest
import github.mrh0.eclang.ast.token.data.TBoolean
import github.mrh0.eclang.ast.token.op.requireBool
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.Narrowing
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.loop.IRStatementWhile
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** `while condition do`, optionally with an `else` block that runs when the loop ends without `break`. */
class TStatementWhile(location: Loc, private val condition: ITok, private val body: ITok, private val elseBody: ITok?, private val assigned: Set<String>) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        // The condition is checked again after each round, so it can't rely on what the body changes.
        cd.forgetAssigned(location, assigned)
        val conditionIR = requireBool(cd, location, "The `while` condition",
            cd.noHoist("`try` can't be used in a `while` condition, because it would only run once. Call it inside the loop instead.") { condition.process(cd, Types.None) })
        return Types.None to compileLoop(cd, location, hint, elseBody,
            elseNarrowing = { Narrowing.apply(cd, Narrowing.of(cd, condition, false)) },
            endless = isTrue(condition),
            compileBody = {
                cd.ctx().push()
                Narrowing.apply(cd, Narrowing.of(cd, condition, true))
                body.process(cd, hint).second.also { cd.ctx().pop() }
            },
        ) { IRStatementWhile(location, conditionIR, it) }
    }

    private fun isTrue(condition: ITok): Boolean = when (condition) {
        is TBoolean -> condition.value
        is TExprNest -> isTrue(condition.expr)
        else -> false
    }

    override fun toString(): String {
        return "TStatementWhile($condition, $body)"
    }
}
