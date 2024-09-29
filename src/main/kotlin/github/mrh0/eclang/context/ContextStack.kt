package github.mrh0.eclang.context

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.state.IVar
import github.mrh0.eclang.error.EcAssignTypeError
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.error.EcNotDefinedError
import github.mrh0.eclang.types.EcType

class ContextStack {
    class Scope(val vars: MutableMap<String, Int> = mutableMapOf(), val deferred: MutableList<ITok> = mutableListOf()) {
        var returned: Boolean = false
        val newDeferred: MutableList<ITok> = mutableListOf()
    }

    val vars: MutableList<IVar> = mutableListOf()
    val scopeStack: MutableList<Scope> = mutableListOf(Scope())

    fun define(location: Loc, variable: IVar): IVar {
        val top = scopeStack.last().vars
        if(top.containsKey(variable.getName())) throw EcError(location, "${variable.getName()} is already defined in this context.")
        top[variable.getName()] = vars.size
        vars.add(variable)
        return variable
    }

    fun defer(location: Loc, tok: ITok) {
        val top = scopeStack.last()
        top.deferred.add(tok)
        top.newDeferred.add(tok)
    }

    fun getRaw(location: Loc, name: String): IVar? {
        val top = scopeStack.last().vars
        return if(top.containsKey(name)) vars[top[name]!!] else null
    }

    fun get(location: Loc, name: String): IVar {
        val top = scopeStack.last().vars
        return if(top.containsKey(name)) vars[top[name]!!] else throw EcNotDefinedError(location, name)
    }

    fun getIndex(location: Loc, name: String): Int {
        val top = scopeStack.last().vars
        return if(top.containsKey(name)) top[name]!! else throw EcNotDefinedError(location, name)
    }

    fun assign(location: Loc, name: String, type: EcType): IVar {
        val top = scopeStack.last().vars
        if(!top.containsKey(name)) throw EcNotDefinedError(location, name)
        if(type != get(location, name).getType()) throw EcAssignTypeError(location, name, get(location, name).getType(), type)
        return vars[top[name]!!]
    }

    fun push() {
        val top = scopeStack.last()
        scopeStack.add(Scope(top.vars.toMutableMap(), top.deferred.toMutableList()))
    }

    fun pop(): Scope = scopeStack.removeLast()

    fun setReturned() {
        val top = scopeStack.last()
        top.returned = true
    }

    fun hasReturned(): Boolean {
        val top = scopeStack.last()
        return top.returned
    }

    fun getVars() = vars.toTypedArray()

    fun getDeferred(): MutableList<ITok> {
        val top = scopeStack.last()
        return top.deferred
    }

    fun getNewDeferred(): MutableList<ITok> {
        val top = scopeStack.last()
        return top.newDeferred
    }
}