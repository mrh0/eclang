package github.mrh0.eclang.context.function

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.token.TBlock
import github.mrh0.eclang.context.state.Variable
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.ir.IRNop
import github.mrh0.eclang.ir.function.IRFunctionOverride
import github.mrh0.eclang.ir.function.IRParameter
import github.mrh0.eclang.ir.function.IRParameters
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeCallSignature

class FunctionOverride(val location: Loc, val id: String, val params: Array<FunctionParameter>, val ret: EcType, val block: TBlock?, private var called: Boolean = false) {
    fun match(location: Loc, types: Array<EcType>): Boolean {
        if(types.size != params.size) return false
        for(i in types.indices) {
            if(!types[i].accepts(location, params[i].type)) return false
        }
        return true
    }

    fun getType() = EcTypeCallSignature(params.map { it.type }.toTypedArray(), ret)

    fun isExternal() = block == null

    override fun toString() = "FO($id, ${params.map { it.toString() }}, $ret)"

    fun buildIR(location: Loc, cd: CompileData, hint: EcType): IIR {
        cd.newContext(id)
        params.forEach { cd.ctx().define(location, Variable(it.name, it.type)) }

        if (isExternal()) return IRNop(location)
        return IRFunctionOverride(location, block?.process(cd, hint)?.second as IRBlock, id, IRParameters(location, params.map { IRParameter(location, it.name, it.type, it.def) }), ret)
    }

    fun hasBeenCalled() = called
    fun setCalled() {
        called = true
        GlobalFunctions.calledFunctionOverrides.put(id, true)
    }
}