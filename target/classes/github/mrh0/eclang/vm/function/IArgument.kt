package github.mrh0.eclang.vm.function

import github.mrh0.eclang.types.EcTypeAny
import github.mrh0.eclang.values.GsBase

interface IArgument {
    fun getName(): String
    fun getType(): EcTypeAny
    fun match(value: GsBase): Boolean
    fun match(type: EcTypeAny): Boolean
}