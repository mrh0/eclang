package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRBool
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

class TBoolean(location: Loc, val value: Boolean) : Tok(location) {
    override fun pointerFlow(): PointerFlow = PointerFlow.Nothing

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        return Types.Bool to IRBool(location, value)
    }

    override fun toString(): String {
        return "TBoolean($value)"
    }
}
