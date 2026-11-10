package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRCString
import github.mrh0.eclang.util.StringLiteral
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

class TCString(location: Loc, private val value: StringLiteral) : Tok(location) {
    override fun pointerFlow(): PointerFlow = PointerFlow.Nothing

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        return Types.CString to IRCString(location, value)
    }

    override fun toString(): String {
        return "TCString($value)"
    }
}
