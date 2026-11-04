package github.mrh0.eclang.ast

import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.ect2.Type

object TDeadEnd : Tok(Loc.IDENTITY) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        throw Exception("Processing dead end should not occur")
    }
}