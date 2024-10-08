package github.mrh0.eclang.ast.token.op

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.error.EcNotReferenceTypeError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRPass
import github.mrh0.eclang.ir.data.IRReadPointer
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeNull
import github.mrh0.eclang.types.EcTypeNullable
import github.mrh0.eclang.types.EcTypePointer

class TCastNotNull(location: Loc, val expr: ITok) : Tok(location) {
    override fun toString(): String {
        return "TCastNotNull($expr)"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val res = expr.process(cd, hint)
        if (res.first is EcTypeNull) throw EcError(location, "Expression will always be null")
        if (res.first !is EcTypeNullable) return res
        return (res.first as EcTypeNullable).wrapped to res.second
    }
}
