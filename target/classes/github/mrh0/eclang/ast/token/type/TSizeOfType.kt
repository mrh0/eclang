package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeNull

class TSizeOfType(location: Loc, val type: ITok) : Tok(location) {
    override fun toString() = "sizeof($type)"

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val res = type.process(cd, hint)
        val nullable = EcType.nullable(res.first)
        return nullable to IRType(location, nullable)
    }
}