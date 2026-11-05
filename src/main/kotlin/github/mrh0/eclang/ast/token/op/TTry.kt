package github.mrh0.eclang.ast.token.op

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.Lifetimes
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRPass
import github.mrh0.eclang.ir.IRRaw
import github.mrh0.eclang.ir.IRTryPropagate
import github.mrh0.ect2.Result
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * `try f()`: the value of f(), and if f failed, the current function stops and returns the same error (after
 * running its deferred statements). The current function must declare a `throws` type the error fits.
 */
class TTry(location: Loc, val expr: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val (t, ir) = expr.process(cd, hint)
        if (t !is Result) throw EcError(location, "`try` is for calls that can fail, but this is a $t, which can't fail. Remove the `try`.")
        val fn = cd.function ?: throw EcError(location, "`try` can only be used inside a function that declares `throws` (not in a lambda or at the top level). Handle the error here with `?? default` instead.")
        val outerError = fn.throws ?: throw EcError(location, "`try` passes the error on, but ${fn.name} doesn't declare that it can fail. " +
            "Add `throws ${t.error}` to ${fn.name}, or handle the error here with `try ... catch e do` or `?? default`.")
        if (!cd.types.accepts(outerError, t.error))
            throw EcError(location, "This call can fail with ${t.error}, but ${fn.name} only throws $outerError: ${cd.types.explain(t.error, outerError)}.")
        val temp = cd.temp()
        val (deferred, freesPool) = cd.ctx().stack.onExit { cd.compileDeferred(cd.ctx().getDeferred().reversed()) }
        // The error may be allocated in a pool that leaving frees, of a `pool do` block or a deferred `freePool`: pass on a copy instead.
        val keptIn = if (cd.poolBlocks.isNotEmpty()) cd.poolEntry else if (freesPool) TNamed.CURRENT_POOL else null
        val copyInto = keptIn.takeIf { Lifetimes.holdsPointer(outerError) }
        cd.hoist(location, IRTryPropagate(location, temp, t, ir, cd.types.result(fn.returns, outerError), deferred, copyInto))
        if (t.value === Types.None) return Types.None to IRPass(location, null)
        return t.value to IRRaw(location, "$temp.value")
    }

    override fun toString(): String {
        return "TTry($expr)"
    }
}
