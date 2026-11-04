package github.mrh0.eclang.ast.token

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRPass
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** `errdefer statement`: like `defer`, but only runs when the scope is left by an error (`throw`, or `try` passing one on). */
class TErrDefer(location: Loc, val statement: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val fn = cd.function ?: throw EcError(location, "`errdefer` can only be used inside a function that declares `throws` (not in a lambda or at the top level). Use `defer` instead.")
        if (fn.throws == null) throw EcError(location, "`errdefer` runs when an error leaves the function, but ${fn.name} doesn't declare `throws`, so it would never run. Use `defer`, or add `throws` to ${fn.name}.")
        cd.ctx().defer(location, OnError(location, statement))
        return Types.None to IRPass(location, null)
    }

    override fun toString() = "TErrDefer($statement)"

    /** A deferred statement that only error exits run; see [normal]. */
    private class OnError(location: Loc, private val statement: ITok) : Tok(location) {
        override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> = statement.process(cd, hint)
    }

    companion object {
        /** The statements of [deferred] that run when a scope is left without an error. */
        fun normal(deferred: List<ITok>): List<ITok> = deferred.filter { it !is OnError }
    }
}
