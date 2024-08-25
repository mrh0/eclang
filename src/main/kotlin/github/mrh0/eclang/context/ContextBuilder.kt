package github.mrh0.eclang.context

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcAssignTypeError
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.error.EcNotDefinedError
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.context.state.IVar

class ContextBuilder(val contextName: String) {
    private val vars: MutableList<IVar> = mutableListOf()
    private val map: MutableMap<String, Int> = mutableMapOf()
    private var index: Int = 0

    fun define(location: Loc, variable: IVar): IVar {
        if(map.containsKey(variable.getName())) throw EcError(location, "${variable.getName()} is already defined in context $contextName")
        vars.add(variable)
        map[variable.getName()] = index++
        return variable
    }

    fun defineAnonymous(location: Loc, variable: IVar): Int {
        vars.add(variable)
        return index++
    }

    fun getRaw(location: Loc, name: String): IVar? = if(map.containsKey(name)) vars[map[name]!!] else null

    fun get(location: Loc, name: String): IVar {
        return if(map.containsKey(name)) vars[map[name]!!] else throw EcNotDefinedError(location, name)
    }

    fun getIndex(location: Loc, name: String): Int {
        return if(map.containsKey(name)) map[name]!! else throw EcNotDefinedError(location, name)
    }

    fun assign(location: Loc, name: String, type: EcType): Int {
        if(!map.containsKey(name))
            throw EcNotDefinedError(location, name)
        if(type != get(location, name).getType())
            throw EcAssignTypeError(location, name, get(location, name).getType(), type)
        return map[name]!!
    }

    fun build(): Context {
        return Context(contextName, vars.toTypedArray())
    }
}