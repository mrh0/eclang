package github.mrh0.eclang.ast.token.statement

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.op.TNoValue
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.Lifetimes
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.ir.IRStatement
import github.mrh0.eclang.ir.data.IRResultValue
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

class TStatementReturn(location: Loc, val next: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val fn = cd.function ?: throw EcError(location, "`ret` can only be used inside a function.")
        val res = next.process(cd, fn.returns)
        val result = fn.throws?.let { cd.types.result(fn.returns, it) }
        // `ret panic("...")`: the call never comes back, so nothing is returned (and no deferred statement runs).
        if (res.first === Types.Never) {
            cd.ctx().setReturned()
            return Types.None to IRStatement(location, res.second)
        }
        if (fn.returns === Types.Never) throw EcError(location, "Function ${fn.name} is declared `Never`: it never returns, so it can't `ret`. End it with something that doesn't come back, such as `exit(1)`, or give it another return type.")

        if (fn.returns === Types.None) {
            if (res.first !== Types.None) throw EcError(location, "Function ${fn.name} returns nothing, but this returns a ${res.first}. Declare the return type, e.g. `fn ${fn.name}(...): ${cd.types.concrete(res.first)}`.")
            // `ret log(x)` in a function returning nothing: run the call, then return.
            if (result == null) return Types.None to returnWithDefers(cd, location, res.second, Types.None, hasValue = false)
            val ret = returnWithDefers(cd, location, IRResultValue(location, result, false, null), result)
            return Types.None to IRBlock(location, listOf(IRStatement(location, res.second), ret), listOf())
        }
        if (next is TNoValue) throw EcError(location, "Function ${fn.name} returns a ${fn.returns}, so `ret` needs a value: `ret x`.")
        val value = cd.expect(location, res, fn.returns, "return value of '${fn.name}'")
        Lifetimes.checkReturn(cd, location, next, fn.returns)
        val leaving = Lifetimes.leaving(cd, next, fn.returns)
        val check = { Lifetimes.checkLeaving(cd, location, leaving) }
        if (result != null) return Types.None to returnWithDefers(cd, location, IRResultValue(location, result, false, value), result, onExit = check)
        return Types.None to returnWithDefers(cd, location, value, fn.returns, onExit = check)
    }

    override fun toString(): String {
        return "TRet($next)"
    }
}
