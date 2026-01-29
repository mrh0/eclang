package github.mrh0.eclang.context

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.state.IVar
import github.mrh0.eclang.types.EcType

class CompileData {
    var module: Module = Module.get("default")
    // var namespace: String = "default"
    private val localContext: MutableMap<String, Context> = mutableMapOf()

    private var currentLocalContextBuilder: ContextBuilder = module.globalContext
    var genericContext: Map<String, EcType>? = null

    fun getVar(location: Loc, name: String): IVar {
        return ctx().getRaw(name) ?: getGlobal().get(location, name)
    }

    fun newContext(contextName: String, generics: Map<String, EcType> = mapOf()): ContextBuilder {
        localContext[currentLocalContextBuilder.contextName] = currentLocalContextBuilder.build()
        genericContext = generics
        currentLocalContextBuilder = ContextBuilder(contextName)
        return currentLocalContextBuilder
    }

    fun ctx() = currentLocalContextBuilder
    fun getGlobal() = module.globalContext

    fun finalize(): Context {
        localContext[currentLocalContextBuilder.contextName] = currentLocalContextBuilder.build()
        return module.globalContext.build()
    }
}