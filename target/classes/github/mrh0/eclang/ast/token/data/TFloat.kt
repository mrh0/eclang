package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRNumber
import github.mrh0.ect2.Type

/** A decimal literal; Double unless used where a Float is expected. */
class TFloat(location: Loc, val value: Double) : Tok(location) {
    override fun pointerFlow(): PointerFlow = PointerFlow.Nothing

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val type = cd.types.floatLiteral(value)
        return type to IRNumber(location, value, type)
    }

    override fun toString(): String {
        return "TFloat($value)"
    }
}
