package github.mrh0.eclang.ast.token.statement

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRStatement
import github.mrh0.eclang.types.EcType

class TStatement(location: Loc, val next: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val ir = next.process(cd, hint)
        return Pair(ir.first, IRStatement(location, ir.second))
    }

    override fun toString(): String {
        return "$next"
    }
}