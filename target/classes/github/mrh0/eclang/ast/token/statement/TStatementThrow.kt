package github.mrh0.eclang.ast.token.statement

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.NonValueTok
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.Lifetimes
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRResultValue
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** `throw value`: returns the error from the current function, which must declare `throws`. */
class TStatementThrow(location: Loc, val next: ITok) : NonValueTok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val fn = cd.function ?: throw EcError(location, "`throw` can only be used inside a function.")
        val errorType = fn.throws ?: throw EcError(location, "Function ${fn.name} can't throw: add `throws <ErrorType>` after its return type, e.g. `fn ${fn.name}(...): ${fn.returns} throws Error`.")
        val error = cd.expect(location, next.process(cd, errorType), errorType, "error thrown from '${fn.name}'")
        Lifetimes.checkReturn(cd, location, next, errorType, "throw")
        val result = cd.types.result(fn.returns, errorType)
        val leaving = Lifetimes.leaving(cd, next, errorType)
        return Types.None to returnWithDefers(cd, location, IRResultValue(location, result, true, error), result, error = true, onExit = { Lifetimes.checkLeaving(cd, location, leaving, "throw") })
    }

    override fun toString(): String {
        return "TThrow($next)"
    }
}
