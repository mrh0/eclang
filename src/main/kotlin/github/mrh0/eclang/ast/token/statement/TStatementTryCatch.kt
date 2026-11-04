package github.mrh0.eclang.ast.token.statement

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.state.Constant
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRTryCatch
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.Result
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** `try f() catch e do ...`: runs f(); if it failed, runs the block with `e` set to the error. */
class TStatementTryCatch(location: Loc, val expr: ITok, val errorName: String, val body: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val (t, ir) = expr.process(cd, Types.None)
        if (t !is Result) throw EcError(location, "`try ... catch` is for calls that can fail, but this is a $t, which can't fail.")
        val name = testIdentifier(location, errorName)
        cd.ctx().push()
        cd.ctx().define(location, Constant(name, t.error, cd.localCName(name), kind = "a caught error"))
        val bodyIr = body.process(cd, Types.None).second
        cd.ctx().pop()
        return Types.None to IRTryCatch(location, cd.temp(), t, ir, cd.localCName(name), bodyIr)
    }

    override fun toString(): String {
        return "TStatementTryCatch($expr, $errorName, $body)"
    }
}
