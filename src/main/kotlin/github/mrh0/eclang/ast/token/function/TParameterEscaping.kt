package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.function.Parameter
import github.mrh0.eclang.error.EcError

/**
 * `escaping p: T`: the function keeps p after it returns where the compiler can't follow it, such as another thread
 * (`thread.spawn`) or a C library. Each call checks that the argument outlives the caller: it may not point to the
 * caller's local variables, to the pool of a `pool do` block, or into a pool the caller made (see `Lifetimes.noteCall`).
 * The callee counts as storing it in a global (`Escapes`), so the caller's own callers are checked the same way.
 */
class TParameterEscaping(location: Loc, val inner: TParameter) : TParameter(location, inner.name) {
    override fun toFunctionParameter(cd: CompileData): Parameter {
        val p = inner.toFunctionParameter(cd)
        if (p.isMut) throw EcError(location, "`escaping` can't go with `${if (p.isOut) "out" else "mut"}`: '${p.name}' is the caller's variable, which the function only uses during the call. Pass a pointer that lives on instead: `escaping ${p.name}: @T`.")
        if (p.isEscaping) throw EcError(location, "'${p.name}' is marked `escaping` twice.")
        return Parameter(p.location, p.name, p.type, p.default, p.isVarArg, p.isCVarArg, isEscaping = true, isFlat = p.isFlat)
    }

    override fun toString(): String = "TParameterEscaping($inner)"
}
