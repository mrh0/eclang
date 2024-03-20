package github.mrh0.eclang.context.state

import github.mrh0.eclang.types.EcTypeAny

interface IVar {
    fun getName(): String
    fun getType(): EcTypeAny
}