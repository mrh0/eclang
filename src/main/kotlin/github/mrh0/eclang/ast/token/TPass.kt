package github.mrh0.eclang.ast.token

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.NonValueTok
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRPass
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

class TPass (location: Loc) : NonValueTok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        return Types.None to IRPass(location, null)
    }

    override fun toString(): String {
        return "TPass"
    }
}
