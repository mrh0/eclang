package github.mrh0.eclang.context.state

import github.mrh0.eclang.types.EcType

class Imported(private val name: String, private val type: EcType) : IVar {
    override fun getName() = name
    override fun getType() = type
}