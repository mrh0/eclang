package github.mrh0.eclang.context.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.types.EcType

object GlobalFunctions {
    private val functions: MutableMap<String, FunctionOverrides> = mutableMapOf()

    private fun getSourceName(name: String, index: Int): String = "${name}_$index"

    fun addOverride(location: Loc, name: String, args: Array<String>, types: Array<EcType>, returnType: EcType): String {
        return addOverride(location, name, args, types, returnType, null)
    }

    fun addOverride(location: Loc, name: String, args: Array<String>, types: Array<EcType>, returnType: EcType, sourceName: String?): String {
        val fos: FunctionOverrides
        if(functions.containsKey(name)) {
            //if(functions[name]!!.returnType != returnType) throw EcError(location, "Function $name is already defined return type ${functions[name]!!.returnType}")
            fos = functions[name]!!
        }
        else {
            fos = FunctionOverrides(name, returnType)
            functions[name] = fos
        }

        val usedSourceName = sourceName ?: getSourceName(name, fos.getNumberOfOverrides());
        val res = FunctionOverride(usedSourceName, args, types, returnType)
        println("$name $sourceName ${types.map { it.toString() }}")
        fos.add(res)
        return usedSourceName
    }

    fun getOverridesByName(location: Loc, name: String) = functions.getOrElse(name) { throw EcError(location, "No such override $name") }
    fun getOverrides(location: Loc, name: String, types: Array<EcType>) = getOverridesByName(location, name).getMatching(location, types)
}