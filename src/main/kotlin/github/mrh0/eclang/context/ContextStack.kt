package github.mrh0.eclang.context

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.state.IVar
import github.mrh0.eclang.error.EcAssignTypeError
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.error.EcNotDefinedError
import github.mrh0.eclang.types.EcType

class ContextStack {
    val vars: MutableList<IVar> = mutableListOf()
    val scopeStack: MutableList<MutableMap<String, Int>> = mutableListOf(mutableMapOf())

    fun define(location: Loc, variable: IVar): IVar {
        val top = scopeStack.last()
        if(top.containsKey(variable.getName())) throw EcError(location, "${variable.getName()} is already defined in this context.")
        top.put(variable.getName(), vars.size)
        vars.add(variable)
        return variable
    }

    fun getRaw(location: Loc, name: String): IVar? {
        val top = scopeStack.last()
        return if(top.containsKey(name)) vars[top[name]!!] else null
    }

    fun get(location: Loc, name: String): IVar {
        val top = scopeStack.last()
        return if(top.containsKey(name)) vars[top[name]!!] else throw EcNotDefinedError(location, name)
    }

    fun getIndex(location: Loc, name: String): Int {
        val top = scopeStack.last()
        return if(top.containsKey(name)) top[name]!! else throw EcNotDefinedError(location, name)
    }

    fun assign(location: Loc, name: String, type: EcType): Int {
        val top = scopeStack.last()
        if(!top.containsKey(name))
            throw EcNotDefinedError(location, name)
        if(type != get(location, name).getType())
            throw EcAssignTypeError(location, name, get(location, name).getType(), type)
        return top[name]!!
    }

    fun push() {
        val top = scopeStack.last()
        scopeStack.add(top.toMutableMap())
    }

    fun pop() {
        scopeStack.removeLast()
    }

    fun getVars() = vars.toTypedArray()
}