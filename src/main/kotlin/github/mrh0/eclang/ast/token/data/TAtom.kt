package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRAtom
import github.mrh0.eclang.types.EcTypeAtom
import github.mrh0.eclang.types.EcType

class TAtom(location: Loc, private val value: String) : Tok(location) {
    override fun toString(): String {
        return "TAtom(${value})"
    }

    override fun process(cd: CompileData): Pair<EcType, IIR> {
        return EcTypeAtom to IRAtom(location, value)
    }
}