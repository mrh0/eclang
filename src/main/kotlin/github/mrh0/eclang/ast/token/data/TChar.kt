package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRChar
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

class TChar(location: Loc, private val value: String) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        return Types.Char to IRChar(location, value.substring(1, value.length-1))
    }

    override fun toString(): String {
        return "TChar($value)"
    }
}
