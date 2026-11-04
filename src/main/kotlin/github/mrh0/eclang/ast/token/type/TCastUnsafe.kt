package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRConvert
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** `x !as T` (or `x as unsafe T`): a plain C cast. Nothing is checked; use it only when `as` can't do it. */
class TCastUnsafe(location: Loc, val expr: ITok, val type: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val to = cd.storable(location, type.process(cd, Types.None).first, "`!as`")
        val (from, ir) = cd.usable(location, expr.process(cd, Types.None), "`!as`")
        return to to IRConvert(location, ir, from, to, IRConvert.Mode.UNSAFE)
    }

    override fun toString(): String {
        return "TCastUnsafe($expr, $type)"
    }
}
