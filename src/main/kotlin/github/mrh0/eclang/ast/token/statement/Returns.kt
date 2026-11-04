package github.mrh0.eclang.ast.token.statement

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRStatementReturn
import github.mrh0.eclang.ir.IRStatementReturnDefer
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** Leaving the function: runs the deferred statements of every enclosing block (innermost first), then returns. */
internal fun returnWithDefers(cd: CompileData, location: Loc, value: IIR?, type: Type, hasValue: Boolean = true): IIR {
    cd.ctx().setReturned()
    val deferred = cd.noHoist("`try` can't be used in `defer`.") {
        cd.ctx().getDeferred().reversed().map { it.process(cd, Types.None).second }
    }
    if (deferred.isEmpty()) return IRStatementReturn(location, value, hasValue)
    return IRStatementReturnDefer(location, value, type, deferred, cd.temp(), hasValue)
}
