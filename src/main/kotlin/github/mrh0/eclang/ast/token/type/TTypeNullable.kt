package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.types.BuiltInTypes
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeNullable
import github.mrh0.eclang.types.EcTypeUnion

class TTypeNullable(location: Loc, val type: ITok) : Tok(location) {
    override fun toString() = "#nullable($type)"

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val res = type.process(cd, hint)
        if (!res.first.isReferenceType()) throw EcError(location, "Non-reference type '${res.first}' cannot be nullable")
        val nullable = EcTypeNullable(res.first)
        return nullable to IRType(location, nullable)
    }
}