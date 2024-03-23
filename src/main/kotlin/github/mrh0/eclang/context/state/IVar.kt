package github.mrh0.eclang.context.state

import github.mrh0.eclang.types.EcType

interface IVar {
    fun getName(): String
    fun getType(): EcType
}