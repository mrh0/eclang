package github.mrh0.eclang.ast.token.loop

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.Lifetimes
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.loop.IRContinue
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** `continue`, or `continue outer` to start the next round of the loop labelled `outer` around this one. */
class TStatementContinue (location: Loc, private val label: String? = null) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        if (cd.loopDepth == 0) throw EcError(location, "`continue` can only be used inside a loop.")
        val deferred = cd.loopDeferred(location, label, "continue")
        Lifetimes.roundEnds(cd, cd.loopsOut(location, label, "continue"))
        cd.ctx().setReturned()
        return Types.None to IRContinue(location, deferred, cd.continueLabel(location, label))
    }

    override fun toString(): String {
        return "TStatementContinue"
    }
}
