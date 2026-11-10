package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.function.Parameter
import github.mrh0.eclang.error.EcError

/**
 * `own p: Pool`: the function takes the pool over. It may free it (`destroy p`, `clearPool(p)`) as if it had made it, and
 * the caller must have made it (or own it too), and can't use it, or anything in it, after the call (see TExprCall.noteCall).
 */
class TParameterOwn(location: Loc, val inner: TParameter) : TParameter(location, inner.name) {
    override fun toFunctionParameter(cd: CompileData): Parameter {
        val p = inner.toFunctionParameter(cd)
        if (p.isMut) throw EcError(location, "`own` can't go with `${if (p.isOut) "out" else "mut"}`: write `own ${p.name}: Pool`, which gives the function the pool itself.")
        if (p.isOwn) throw EcError(location, "'${p.name}' is marked `own` twice.")
        if (p.isEscaping) throw EcError(location, "`own` can't go with `escaping`: a pool the function takes over is its own to keep.")
        if (p.type !== cd.types.pool)
            throw EcError(location, "`own` takes over a pool, so '${p.name}' must be a `Pool`, not a ${p.type}. To keep a value after the call, give it a pool of its own and pass that.")
        return Parameter(p.location, p.name, p.type, p.default, p.isVarArg, p.isCVarArg, isOwn = true)
    }

    override fun toString(): String = "TParameterOwn($inner)"
}
