package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.function.Parameter
import github.mrh0.eclang.error.EcError

/**
 * `kept p: T`, in a `declare fn` only: the C function keeps p in what its other pointer arguments point to (`apr_hash_set` keeps
 * the key and the value in the hash table), for as long as those live. Each call is then checked like a call of an eclang function
 * that stores p there (see Escapes): p must live at least as long as they do. A C function that keeps p where nothing else
 * says how long (another thread, a global) takes it as `escaping` instead.
 */
class TParameterKept(location: Loc, val inner: TParameter) : TParameter(location, inner.name) {
    override fun toFunctionParameter(cd: CompileData): Parameter {
        val p = inner.toFunctionParameter(cd)
        if (p.isMut) throw EcError(location, "`kept` can't go with `${if (p.isOut) "out" else "mut"}`: the caller's variable is only used during the call. Pass a pointer instead: `kept ${p.name}: @T`.")
        if (p.isKept) throw EcError(location, "'${p.name}' is marked `kept` twice.")
        if (p.isEscaping || p.isOwn) throw EcError(location, "`kept` can't go with `${if (p.isOwn) "own" else "escaping"}`: choose where the function keeps '${p.name}'.")
        return Parameter(p.location, p.name, p.type, p.default, p.isVarArg, p.isCVarArg, isKept = true)
    }

    override fun toString(): String = "TParameterKept($inner)"
}
