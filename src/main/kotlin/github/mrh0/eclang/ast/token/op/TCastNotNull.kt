package github.mrh0.eclang.ast.token.op

import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRConvert
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types
import github.mrh0.ect2.Union

/** `x!`: x, which must not be null. Checked at runtime: if x is null the program stops with an error. */
class TCastNotNull(location: Loc, val expr: ITok) : Tok(location) {
    override fun pointerFlow(): PointerFlow = PointerFlow.From(expr)

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val (t, ir) = cd.usable(location, expr.process(cd, hint), "`!`")
        if (t === Types.Null) throw EcError(location, "This is always null, so `!` would always fail.")
        if (t !is Union || !t.isNullable) throw EcError(location, "A value of type $t is never null; remove the `!`.")
        val nonNull = cd.types.nonNull(t)
        return nonNull to IRConvert(location, ir, t, nonNull, IRConvert.Mode.EXPLICIT)
    }

    override fun toString(): String {
        return "TCastNotNull($expr)"
    }
}
