package github.mrh0.eclang.vm

import github.mrh0.eclang.ast.CompileData

class VM(cd: CompileData) {
    private val contexts: MutableMap<String, Context> = cd.contextMap

    fun getContext(name: String) = contexts[name]
    fun getContextOrDefault(name: String, default: Context) = contexts[name] ?: default
}