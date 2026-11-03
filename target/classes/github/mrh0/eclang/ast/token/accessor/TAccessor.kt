package github.mrh0.eclang.ast.token.accessor

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.error.EcUndefinedAccessorPropertyError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.accessor.IRAccessorArray
import github.mrh0.eclang.ir.accessor.IRAccessorCArray
import github.mrh0.eclang.types.*

class TAccessor(location: Loc, val expr: ITok, val index: ITok) : Tok(location) {
    override fun toString(): String {
        return "($expr[$index])"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val indexRes = index.process(cd, hint)
        val res = expr.process(cd, hint)

        val accessorType = res.first.accessor(location, indexRes.first)

        return when (res.first) {
            is EcTypeArray -> accessorType to IRAccessorArray(location, res.second, indexRes.second)
            is EcTypeCArray -> accessorType to IRAccessorCArray(location, res.second, indexRes.second)
            else -> throw EcUndefinedAccessorPropertyError(location, "[${indexRes.first}]", res.first)
        }
    }
}
