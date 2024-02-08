package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.type.TTypeByName
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRDeadEnd
import github.mrh0.eclang.types.EcTypeAny

class TArgument (location: Loc, val name: String, val type: TTypeByName) : Tok(location) {
    override fun toString() = "$name:$type"

    override fun process(cd: CompileData): Pair<EcTypeAny, IIR> = Pair(type.process(cd).first, IRDeadEnd)
}