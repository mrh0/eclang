package github.mrh0.eclang.ast.token.accessor

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.accessor.IRAccessorNamed
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.util.Util

class TAccessorNamed(location: Loc, val expr: ITok, val name: String) : Tok(location) {
    override fun toString(): String {
        return "($expr.$name)"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val fixedName = Util.testIdentifier(location, name)
        val res = expr.process(cd, hint);
        val propType = res.first.getProperty(location, fixedName)
        return propType to IRAccessorNamed(location, res.second, fixedName)
    }
}
