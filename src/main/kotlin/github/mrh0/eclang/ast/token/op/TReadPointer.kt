package github.mrh0.eclang.ast.token.op

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.error.EcNotReferenceTypeError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRReadPointer
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypePointer

class TReadPointer(location: Loc, val expr: ITok) : Tok(location) {
    override fun toString(): String {
        return "TAddressOf($expr)"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val res = expr.process(cd, hint)
        if (res.first !is EcTypePointer) throw EcNotReferenceTypeError(location, res.first)
        return (res.first as EcTypePointer).wrapped to IRReadPointer(location, res.second)
    }
}
