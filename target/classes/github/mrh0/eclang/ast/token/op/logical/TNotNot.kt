package github.mrh0.eclang.ast.token.op.logical

import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.op.Ops
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRIs
import github.mrh0.eclang.ir.data.IRBool
import github.mrh0.eclang.ir.logical.not.IRNotBool
import github.mrh0.eclang.ir.logical.not.IRNotInt
import github.mrh0.eclang.ir.logical.notnot.IRNotNotInt
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types
import github.mrh0.ect2.Union

/** `!!x` is true for true, non-zero numbers and non-null values; `!x` is the opposite. */
class TNotNot(location: Loc, val expr: ITok) : Tok(location) {
    override fun pointerFlow(): PointerFlow = PointerFlow.Nothing // a number or Bool

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val (t, ir) = cd.usable(location, expr.process(cd, Types.None), "`!!`")
        val set = true
        return Types.Bool to when {
            t === Types.Bool -> if (set) ir else IRNotBool(location, ir)
            Ops.isNumeric(t) -> if (set) IRNotNotInt(location, ir) else IRNotInt(location, ir)
            t === Types.Null -> IRBool(location, !set)
            t is Union && t.isNullable -> IRIs(location, ir, t, Types.Null, negated = set)
            else -> throw EcError(location, "`!!` needs a Bool, a number or a nullable value, but got $t.")
        }
    }

    override fun toString(): String {
        return "TNotNot($expr)"
    }
}
