package github.mrh0.eclang.ast.token.global

import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.state.Constant
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.error.EcDefineTypeError
import github.mrh0.eclang.ir.IRGlobalDefineConst

class TGlobalDefineConstTyped(location: Loc, private val varName: String, private val expr: ITok, private val type: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val fixedName = testIdentifier(location, varName)
        val typeIr = type.process(cd, hint)
        val ir = expr.process(cd, typeIr.first)
        if (!typeIr.first.accepts(location, ir.first)) throw EcDefineTypeError(location, fixedName, typeIr.first, ir.first)
        val ivar = cd.getGlobal().define(location, Constant(fixedName, typeIr.first))
        return Pair(ir.first, IRGlobalDefineConst(location, ivar, ir.second))
    }

    override fun toString(): String {
        return "TGlobalDefineConstTyped($expr)"
    }
}