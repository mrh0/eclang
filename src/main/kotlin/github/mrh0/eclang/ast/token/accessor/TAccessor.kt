package github.mrh0.eclang.ast.token.accessor

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.error.EcOpTypeError
import github.mrh0.eclang.error.EcUndefinedAccessorPropertyError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.ir.accessor.IRAccessorNamed
import github.mrh0.eclang.ir.arithmetic.add.IRAdd
import github.mrh0.eclang.ir.branch.IRNullishCoalescing
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeBool
import github.mrh0.eclang.types.EcTypeNullable
import github.mrh0.eclang.util.Util

class TAccessor(location: Loc, val expr: ITok, val index: ITok) : Tok(location) {
    override fun toString(): String {
        return "($expr[$index])"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val indexRes = index.process(cd, hint)
        val res = expr.process(cd, hint)
        val propType = res.first.getProperty(location, fixedName) ?: throw EcUndefinedAccessorPropertyError(location, fixedName, res.first)
        return propType to IRAccessorNamed(location, res.second, fixedName)
    }
}
