package github.mrh0.eclang.context.function

import github.mrh0.eclang.types.EcTypeAny

interface IArgument {
    fun getName(): String
    fun getType(): EcTypeAny
    fun match(type: EcTypeAny): Boolean
}