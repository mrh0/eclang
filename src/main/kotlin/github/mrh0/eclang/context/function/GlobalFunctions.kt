package github.mrh0.eclang.context.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.token.TBlock
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.error.EcInvalidVarArgError
import github.mrh0.eclang.types.EcType

object GlobalFunctions {
    private val functions: MutableMap<String, FunctionOverrides> = mutableMapOf()
    val calledFunctionOverrides: MutableMap<String, Boolean> = mutableMapOf()

    private fun getSourceName(name: String, index: Int): String = "${name}_$index"

    fun addOverride(location: Loc, name: String, params: Array<FunctionParameter>, returnType: EcType): String {
        return addOverride(location, name, params, returnType, null, null, null)
    }

    fun addOverride(location: Loc, name: String, params: Array<FunctionParameter>, returnType: EcType, block: TBlock?, sourceName: String?, generics: Map<String, EcType>?, called: Boolean = false): String {
        val fos: FunctionOverrides

        var varArg: FunctionParameter? = null
        var newParams = params
        if (params.isNotEmpty()) {
            varArg = params.find { it.varArg }
            if (varArg != null && varArg != params.last()) throw EcInvalidVarArgError(location, name)
            if (varArg != null) newParams = params.dropLast(1).toTypedArray()
        }

        if(functions.containsKey(name)) {
            //if(functions[name]!!.returnType != returnType) throw EcError(location, "Function $name is already defined return type ${functions[name]!!.returnType}")
            fos = functions[name]!!
        }
        else {
            fos = FunctionOverrides(name, returnType)
            functions[name] = fos
        }

        val usedSourceName = sourceName ?: getSourceName(name, fos.getNumberOfOverrides());
        val res = FunctionOverride(location, usedSourceName, newParams, returnType, block, generics ?: mapOf(), null, varArg?.type)
        fos.add(res)
        if (name == "main") calledFunctionOverrides[usedSourceName] = true
        if (called) calledFunctionOverrides[usedSourceName] = true
        return usedSourceName
    }

    fun getOverridesByName(location: Loc, name: String) = functions.getOrElse(name) { throw EcError(location, "No such override $name") }
    fun getOverrides(location: Loc, name: String, types: Array<EcType>) = getOverridesByName(location, name).getMatching(location, types)
    fun getAllOverrides(location: Loc) = functions.values
}