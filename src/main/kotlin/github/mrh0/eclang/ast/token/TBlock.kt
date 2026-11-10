package github.mrh0.eclang.ast.token

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.NonValueTok
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.branch.TTailIf
import github.mrh0.eclang.ast.token.function.call.TStatementCall
import github.mrh0.eclang.ast.token.statement.TDestructure
import github.mrh0.eclang.ast.token.statement.TStatement
import github.mrh0.eclang.ast.token.statement.TStatementAssign
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

class TBlock(location: Loc, val statements: List<ITok>) : NonValueTok(location) {
    /** For a function body: the locals whose address is taken in it (`TFunc.addressTaken`). */
    var addressTaken: Map<String, Loc> = mapOf()

    override fun toString(): String {
        return "$statements"
    }

    /**
     * Whether the statements after [statement] can be checked even when it failed: it declares no name and teaches
     * flow typing nothing (a call, an assignment, an unconditional `ret`).
     */
    private fun isIndependent(statement: ITok) = statement is TStatementCall || statement is TStatement ||
        statement is TStatementAssign || (statement is TDestructure && statement.assigns) || (statement is TTailIf && !statement.isConditional)

    /**
     * [e], saying why a `debug assert` didn't help when one narrowed a variable before: a release build leaves it out, so the
     * code after it can't rely on it. Only for errors about null or a union, which is what a narrowing would have fixed.
     */
    private fun debugHint(cd: CompileData, e: EcError): EcError {
        val narrowed = cd.ctx().stack.debugNarrowed
        if (narrowed.isEmpty() || !(e.reason.contains("null") || e.reason.contains("?") || e.reason.contains("|"))) return e
        val (name, at) = narrowed.entries.first()
        return EcError(e.location, e.reason + " Note: the `debug` check of '$name' at line ${at.getLine()} doesn't count here, because a release build leaves it out. " +
            "Make it an `assert` to check it in every build.")
    }

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        cd.ctx().push()
        val irs = mutableListOf<IIR>()
        for (statement in statements) {
            if (cd.ctx().hasReturned()) {
                // After a branch picked at compile time (in one copy of a function with union parameters) the
                // rest is simply not needed; otherwise unreachable code is a mistake. After an error, a failed
                // statement may be why the block seems to leave early.
                if (cd.ctx().hasReturnedStatically() || cd.functionHasErrors) break
                throw EcError((statement as? Tok)?.location ?: location, "This code can never run: the code before it always leaves the block.")
            }
            val depth = cd.ctx().depth()
            try {
                val (hoisted, ir) = cd.collectHoisted { statement.process(cd, hint).second }
                irs += hoisted
                irs += ir
            } catch (e: EcError) {
                cd.report(debugHint(cd, e))
                while (cd.ctx().depth() > depth) cd.ctx().pop()
                // The rest of the block may need what the statement would have declared or narrowed; checking it
                // would mostly report follow-up errors. The block is then treated as leaving here.
                if (!isIndependent(statement)) {
                    cd.ctx().setReturned(statically = true)
                    break
                }
            }
        }
        val diverges = cd.ctx().hasReturned()
        val deferredIr = if (!diverges) cd.compileDeferred(TErrDefer.normal(cd.ctx().getNewDeferred()).reversed()) else listOf()
        // A block's statements run whenever the code around it gets here (a branch pushes a scope of its own).
        cd.ctx().stack.pop(ran = true)
        return Types.None to IRBlock(location, irs, deferredIr, diverges)
    }
}
