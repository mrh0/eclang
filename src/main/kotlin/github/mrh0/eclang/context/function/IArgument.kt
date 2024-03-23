package github.mrh0.eclang.context.function

import github.mrh0.eclang.types.EcType

interface IArgument {
    fun getName(): String
    fun getType(): EcType
    fun match(type: EcType): Boolean
}