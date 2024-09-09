package github.mrh0.eclang.ast.token.variable

import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.state.Constant
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRStatementDefine
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.error.EcDefineTypeError

class TStatementDefineConstTyped(location: Loc, private val varName: String, private val expr: ITok, private val type: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        testIdentifier(location, varName)
        val typeIr = type.process(cd, hint)
        val ir = expr.process(cd, typeIr.first)
        if (!typeIr.first.accepts(location, ir.first)) throw EcDefineTypeError(location, varName, typeIr.first, ir.first)
        val ivar = cd.ctx().define(location, Constant(varName, typeIr.first))
        return Pair(ir.first, IRStatementDefine(location, ivar, ir.second))
    }

    override fun toString(): String {
        return "TDefineTyped($expr)"
    }
}