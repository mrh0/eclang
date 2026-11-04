package github.mrh0.eclang.ast.token.loop

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.loop.IRContinue
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

class TStatementContinue (location: Loc) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        if (cd.loopDepth == 0) throw EcError(location, "`continue` can only be used inside a loop.")
        val deferred = cd.loopDeferred()
        cd.ctx().setReturned()
        return Types.None to IRContinue(location, deferred)
    }

    override fun toString(): String {
        return "TStatementContinue"
    }
}
