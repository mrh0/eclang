package github.mrh0.eclang.ast

import github.mrh0.eclang.context.Context
import github.mrh0.eclang.context.ContextBuilder
import github.mrh0.eclang.context.state.IVar
import github.mrh0.eclang.types.EcType

class CompileData {
    var namespace: String = "default"
    private val contextMap: MutableMap<String, Context> = mutableMapOf()
    private val global = ContextBuilder("__GLOBAL__")
    private var currentContextBuilder: ContextBuilder = global
    var genericContext: Map<String, EcType>? = null

    fun getVar(location: Loc, name: String): IVar {
        return ctx().getRaw(location, name) ?: getGlobal().get(location, name)
    }

    fun newContext(contextName: String, generics: Map<String, EcType> = mapOf()): ContextBuilder {
        contextMap[currentContextBuilder.contextName] = currentContextBuilder.build()
        genericContext = generics
        currentContextBuilder = ContextBuilder(contextName)
        return currentContextBuilder
    }

    fun ctx() = currentContextBuilder
    fun getGlobal() = global

    fun finalize(): Context {
        contextMap[currentContextBuilder.contextName] = currentContextBuilder.build()
        return global.build()
    }
}