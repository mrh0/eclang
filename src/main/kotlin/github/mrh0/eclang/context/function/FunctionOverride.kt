package github.mrh0.eclang.context.function

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.token.TBlock
import github.mrh0.eclang.context.state.InlineConstant
import github.mrh0.eclang.context.state.Variable
import github.mrh0.eclang.error.EcAssignTypeError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.ir.IRNop
import github.mrh0.eclang.ir.function.IRFunctionOverride
import github.mrh0.eclang.ir.function.IRParameter
import github.mrh0.eclang.ir.function.IRParameters
import github.mrh0.eclang.types.*

class FunctionOverride(
    val location: Loc,
    val id: String,
    val params: Array<FunctionParameter>,
    val ret: EcType,
    val block: TBlock?,
    private val generics: Map<String, EcType>,
    val throws: EcType?,
    val varArg: EcType?,
    private var called: Boolean = false
) {
    private val noDefParams = params.filter { it.def == null }
    val hasGenerics = params.any { it.type is EcTypeGeneric }

    fun match(location: Loc, types: Array<EcType>): Boolean {
        if(types.size != noDefParams.size && varArg == null) return false
        for(i in types.indices) {
            val pt = params.getOrNull(i)?.type ?: varArg as EcType
            if(!pt.accepts(location, types[i])) return false
        }
        return true
    }

    fun getType() = EcTypeCallSignature(params.map { it.type }.toTypedArray(), ret)

    fun isExternal() = block == null

    override fun toString() = "FO($id, ${params.map { it.toString() }}, $ret)"

    fun buildIR(location: Loc, cd: CompileData, hint: EcType): IIR {
        cd.newContext(id, generics)
        params.forEach {
            if (it.def == null) cd.ctx().define(location, Variable(it.name, it.type))
            else {
                val defPair = it.def.process(cd, hint)
                //if (!defPair.second.deterministic()) throw EcError(location, "Default value of ${it.name} must be deterministic.")
                if (!it.type.accepts(location, defPair.first)) throw EcAssignTypeError(location, it.name, it.type, defPair.first)
                cd.ctx().define(location, InlineConstant(it.name, it.type, defPair.second))
            }
        }

        if (isExternal()) return IRNop(location)
        return IRFunctionOverride(
            location,
            block?.process(cd, hint)?.second as IRBlock,
            id,
            IRParameters(
                location,
                params.map { IRParameter(location, it.name, it.type, it.def?.process(cd, hint)?.second) }
            ),
            ret,
            throws,
            varArg
        )
    }

    fun hasBeenCalled() = called
    fun setCalled() {
        called = true
        GlobalFunctions.calledFunctionOverrides[id] = true
    }
}