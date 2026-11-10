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
import github.mrh0.eclang.ir.branch.IRStatementIf
import github.mrh0.eclang.ir.branch.IRStatementIfElse
import github.mrh0.eclang.ir.data.IRBool
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * `if a do ... else if b do ... else ...`. Each body sees what its condition proves (inside `if x != null do`, x
 * isn't null), and each later branch sees the earlier conditions as false. When every branch but the last always
 * leaves (`ret`, `throw`, ...), the code after the `if` sees all the conditions as false too.
 *
 * A condition the types already decide (see [Narrowing.constant]) selects its branch at compile time: a branch
 * that can't run is skipped entirely, and one that always runs ends the chain.
 */
class TStatementIf(location: Loc, private val conditions: List<ITok>, private val bodies: List<ITok>, private val elseBody: ITok?) : NonValueTok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val conditionIRs = mutableListOf<IIR>()
        val bodyIRs = mutableListOf<IRBlock>()
        var alwaysTaken: IRBlock? = null
        var pushed = 0
        val stack = cd.ctx().stack
        // What each branch knows about pools at its end, for the code after the `if` (see ContextStack.joinBranches).
        val ends = mutableListOf<github.mrh0.eclang.context.RoundEnd>()
        for ((i, condition) in conditions.withIndex()) {
            val constant = Narrowing.constant(cd, condition)
            if (constant != false) {
                val value = if (i == 0 || constant == true) condition.process(cd, Types.None)
                    else cd.noHoist("`try` can't be used in an `else if` condition; assign it to a variable before the `if`.") { condition.process(cd, Types.None) }
                val conditionIR = requireBool(cd, location, "The `if` condition", value)
                cd.ctx().push()
                Narrowing.apply(cd, Narrowing.of(cd, condition, true))
                val body = bodies[i].process(cd, hint).second as IRBlock
                stack.popBranch()?.let { ends += it }
                if (constant == true) { alwaysTaken = body; break }
                conditionIRs += conditionIR
                bodyIRs += body
            }
            // Later branches only run when this condition was false.
            cd.ctx().push()
            pushed++
            Narrowing.apply(cd, Narrowing.of(cd, condition, false))
        }
        val elseIR = alwaysTaken ?: elseBody?.process(cd, hint)?.second as IRBlock?
        // The last "conditions were false" scope is the way past the `if` without a branch, or through `else`, unless that always leaves.
        if (alwaysTaken == null && elseIR?.diverges != true && !stack.hasReturned()) ends += stack.roundEnd()
        repeat(pushed) { stack.popBranch() }
        stack.joinBranches(ends)

        if (bodyIRs.all { it.diverges }) {
            if (elseIR?.diverges == true) cd.ctx().setReturned(statically = alwaysTaken != null)
            else if (alwaysTaken == null) conditions.forEach { Narrowing.apply(cd, Narrowing.of(cd, it, false)) }
        }
        return Types.None to when {
            bodyIRs.isEmpty() && elseIR == null -> IRPass(location, null)
            bodyIRs.isEmpty() -> IRStatementIf(location, listOf(IRBool(location, true)), listOf(elseIR!!))
            elseIR != null -> IRStatementIfElse(location, conditionIRs, bodyIRs, elseIR)
            else -> IRStatementIf(location, conditionIRs, bodyIRs)
        }
    }

    override fun toString(): String {
        return "TStatementIf($conditions)"
    }
}
