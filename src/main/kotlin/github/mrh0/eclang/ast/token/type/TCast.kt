package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.op.Ops
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRConvert
import github.mrh0.ect2.*

/**
 * `x as T`, a conversion that is either safe or checked:
 * - anything that converts implicitly (`Int as Long`, `Int as Int | CString`);
 * - between any two number types (`Long as Int` may cut the value; that's what `as` is for);
 * - a union to one of its members (`x as Int`), checked at runtime;
 * - a record pointer to a record that extends it (`animal as @Cat`), checked at runtime.
 * Anything else needs `!as`, an unchecked C cast.
 */
class TCast(location: Loc, val expr: ITok, val type: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val to = cd.storable(location, type.process(cd, Types.None).first, "`as`")
        val value = cd.usable(location, expr.process(cd, to), "`as`")
        val from = value.first
        val types = cd.types
        if (types.accepts(to, from)) return to to cd.expect(location, value, to, "`as`")
        val allowed = (Ops.isNumeric(from) && Ops.isNumeric(to)) ||
            (from is Union && from.members.any { types.accepts(to, it) }) ||
            (from is Pointer && to is Pointer && from.target is Record && to.target is Record &&
                to.target.distanceTo(from.target) != null)
        if (!allowed) throw EcError(location, "Can't convert $from to $to with `as`: ${types.explain(from, to)}. Use `!as` for an unchecked C cast.")
        return to to IRConvert(location, value.second, from, to, IRConvert.Mode.EXPLICIT)
    }

    override fun toString(): String {
        return "TCast($expr, $type)"
    }
}
