package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRValue
import github.mrh0.eclang.types.EcTypeAtom
import github.mrh0.eclang.types.EcTypeAny
import github.mrh0.eclang.values.GsAtom

class TAtom(location: Loc, private val value: String) : Tok(location) {
    override fun toString(): String {
        return "TAtom(${value})"
    }

    override fun process(cd: CompileData): Pair<EcTypeAny, IIR> {
        return EcTypeAtom to IRValue(location, GsAtom.of(value))
    }
}