package github.mrh0.eclang.ast.token.op

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.error.EcOpTypeError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.ir.branch.IRNullishCoalescing
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeNull

class TNullishCoalescing(location: Loc, val left: ITok, val right: ITok) : Tok(location) {
    override fun toString(): String {
        return "($left ?? $right)"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val l = left.process(cd, hint);
        val r = right.process(cd, hint);
        return when {
            l.first.accepts(location, r.first) -> EcType.notNullable(l.first) to IRNullishCoalescing(location, l.second, r.second, IRType(location, EcType.notNullable(l.first)))
            l.first.eq(EcTypeNull) -> r.first to r.second
            //l.first is EcTypeNullable && l.first.accepts(location, r.first) -> Pair((l.first as EcTypeNullable).wrapped, IRNullishCoalescing(location, l.second, r.second, IRType(location, (l.first as EcTypeNullable).wrapped)))
            else -> throw EcOpTypeError(location, "??", l.first, r.first)
        }
    }
}
