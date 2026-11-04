package github.mrh0.eclang.ast.token.loop

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.loop.IRBreak
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** `break`, or `break outer` to leave the loop labelled `outer` around this one. */
class TStatementBreak (location: Loc, private val label: String? = null) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        if (cd.loopDepth == 0) throw EcError(location, "`break` can only be used inside a loop.")
        val deferred = cd.loopDeferred(location, label, "break")
        cd.ctx().setReturned()
        return Types.None to IRBreak(location, cd.breakLabel(location, label), deferred)
    }

    override fun toString(): String {
        return "TStatementBreak"
    }
}
