package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.function.Parameter
import github.mrh0.eclang.error.EcError

/**
 * `mut p: T`: a parameter the function changes for its caller, who passes a variable as `f(mut x)`. In C it is a `T*`, so it
 * has the type `@T`; the Visitor turns every `p` of the body into `@p`. With [isOut] (`out p: T`, only in `declare fn`), C only
 * writes it, so the caller's variable may be a `T?` of a pointer type that is still null (see `Functions.score`).
 */
class TParameterMut(location: Loc, val inner: TParameter, val isOut: Boolean) : TParameter(location, inner.name) {
    private val word get() = if (isOut) "out" else "mut"

    override fun toFunctionParameter(cd: CompileData): Parameter {
        val p = inner.toFunctionParameter(cd)
        if (p.isVarArg || p.isCVarArg) throw EcError(location, "A variadic parameter can't be `$word`: the extra arguments are values, not variables. Take an array instead, and change its elements.")
        if (p.default != null) throw EcError(location, "`$word` parameter '${p.name}' can't have a default value: the caller must say which variable it changes. Remove the default.")
        if (p.isMut) throw EcError(location, "'${p.name}' is marked twice: write `mut ${p.name}: T` or `out ${p.name}: T`.")
        return Parameter(p.location, p.name, cd.types.pointer(p.type), isMut = true, isOut = isOut)
    }

    override fun toString(): String = "TParameterMut($word, $inner)"
}
