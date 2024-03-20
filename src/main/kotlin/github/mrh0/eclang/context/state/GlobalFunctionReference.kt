package github.mrh0.eclang.context.state

import github.mrh0.eclang.types.EcTypeAny

class GlobalFunctionReference(private val name: String, private val type: EcTypeAny) : IVar {
    override fun getName() = name
    override fun getType() = type
}