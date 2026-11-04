package github.mrh0.eclang.ast.token.data.record

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.record.IRHere
import github.mrh0.ect2.Type

class THere(location: Loc) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        return hint to IRHere(location)
    }

    override fun toString(): String {
        return "THere"
    }
}
