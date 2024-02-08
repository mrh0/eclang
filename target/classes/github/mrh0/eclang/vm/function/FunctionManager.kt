package github.mrh0.eclang.vm.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.types.EcTypeAny
import github.mrh0.eclang.values.GsBase

class FunctionManager {
    companion object {
        val INSTANCE = FunctionManager()

        fun findExactMatch(location: Loc, overrides: List<FunctionOverride>, args: Array<GsBase>): FunctionOverride {
            return overrides.find { it.match(location, args) } ?: TODO("Handle missing runtime matching")
        }
    }
    private val namedFunctionMap: MutableMap<String, FunctionOverrides> = mutableMapOf()

    fun addOverride(location: Loc, name: String, args: Array<String>, types: Array<EcTypeAny>, returnType: EcTypeAny, callable: ICallable): FunctionOverrides {
        val fos: FunctionOverrides
        if(namedFunctionMap.containsKey(name)) {
            //if(namedFunctionMap[name]!!.returnType != returnType) throw GsError(location, "Function $name is already defined return type ${namedFunctionMap[name]!!.returnType}")
            fos = namedFunctionMap[name]!!
        }
        else {
            fos = FunctionOverrides(name)
            namedFunctionMap[name] = fos
        }
        val res = FunctionOverride(name, args, types, returnType, callable)
        println("$name ${types.map { it.toString() }}")
        //println("$name ${types.map { it.toString() }} ${GsTuple(arrayOf(GsInt(1), GsInt(2))).match(location, types[0])}")
        fos.add(res)
        return fos
    }

    fun getOverridesByName(location: Loc, name: String) = namedFunctionMap.getOrElse(name) { throw EcError(location, "No such override $name") }
    fun getOverrides(location: Loc, name: String, types: Array<EcTypeAny>) = getOverridesByName(location, name).getMatching(location, types)
}