package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.function.Parameter
import github.mrh0.eclang.error.EcError

/**
 * `flat p: T`: the argument may be used by another thread, so it must reach no memory but `sync` records: numbers, Bools,
 * records and arrays of a fixed size made of those, and pointers to `sync` records (a Mutex, a Queue). For a pointer parameter
 * (`flat data: @<T>`) that is what it points at; for a function, what a lambda captures. As in Goose, where only values without
 * pointers go from one thread to another, what one thread frees or changes then can't reach the other except through
 * something made for it. Each call is checked (see Sharing); `unsafe` around the call skips that.
 */
class TParameterFlat(location: Loc, val inner: TParameter) : TParameter(location, inner.name) {
    override fun toFunctionParameter(cd: CompileData): Parameter {
        val p = inner.toFunctionParameter(cd)
        if (p.isMut) throw EcError(location, "`flat` can't go with `${if (p.isOut) "out" else "mut"}`: the caller's variable can't go to another thread. Pass a copy, or a pointer to a `sync` record.")
        if (p.isFlat) throw EcError(location, "'${p.name}' is marked `flat` twice.")
        if (p.isOwn || p.isKept) throw EcError(location, "`flat` can't go with `${if (p.isOwn) "own" else "kept"}`.")
        return Parameter(p.location, p.name, p.type, p.default, p.isVarArg, p.isCVarArg, isEscaping = p.isEscaping, isFlat = true)
    }

    override fun toString(): String = "TParameterFlat($inner)"
}
