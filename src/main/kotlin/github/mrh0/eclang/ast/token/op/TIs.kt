package github.mrh0.eclang.ast.token.op

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRIs
import github.mrh0.ect2.Pointer
import github.mrh0.ect2.Record
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types
import github.mrh0.ect2.Union

/**
 * `x is T` (or `x !is T`): whether a union value currently holds a T, or whether a record pointer points at a T
 * (or a record extending T). Inside `if x is T do`, x is a T.
 */
class TIs(location: Loc, val expr: ITok, val type: ITok, val negated: Boolean) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val (t, ir) = cd.usable(location, expr.process(cd, Types.None), "`is`")
        val target = type.process(cd, Types.None).first
        val recordCheck = t is Pointer && target is Pointer && t.target is Record && target.target is Record &&
            target.target.distanceTo(t.target) != null
        val possible = recordCheck || cd.types.accepts(target, t) ||
            (t is Union && t.members.any { it === target || cd.types.isSubtype(it, target) })
        // In a copy of a function with union parameters, the types may decide the check; that's expected there.
        if (!possible && cd.function?.isUnionCopy != true)
            throw EcError(location, "A value of type $t can never be a $target, so this is always ${if (negated) "true" else "false"}.")
        return Types.Bool to IRIs(location, ir, t, target, negated)
    }

    override fun toString(): String {
        return "TIs($expr, $type)"
    }
}
