package github.mrh0.eclang.ast.token.statement

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
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

        if (fn.returns === Types.None) {
            if (res.first !== Types.None) throw EcError(location, "Function ${fn.name} returns nothing, but this returns a ${res.first}. Declare the return type, e.g. `fn ${fn.name}(...): ${cd.types.concrete(res.first)}`.")
            // `ret log(x)` in a function returning nothing: run the call, then return.
            if (result == null) return Types.None to returnWithDefers(cd, location, res.second, Types.None, hasValue = false)
            val ret = returnWithDefers(cd, location, IRResultValue(location, result, false, null), result)
            return Types.None to IRBlock(location, listOf(IRStatement(location, res.second), ret), listOf())
        }
        val value = cd.expect(location, res, fn.returns, "return value of '${fn.name}'")
        Lifetimes.checkReturn(cd, location, next, fn.returns)
        if (result != null) return Types.None to returnWithDefers(cd, location, IRResultValue(location, result, false, value), result)
        return Types.None to returnWithDefers(cd, location, value, fn.returns)
    }

    override fun toString(): String {
        return "TRet($next)"
    }
}
