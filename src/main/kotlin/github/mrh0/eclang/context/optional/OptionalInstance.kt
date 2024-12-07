package github.mrh0.eclang.context.optional

import github.mrh0.eclang.types.EcType

class OptionalInstance private constructor(val type: EcType) {
    companion object {
        private val optionals: MutableMap<Int, OptionalInstance> = mutableMapOf()

        fun get(type: EcType): OptionalInstance {
            return optionals.getOrPut(type.hashCode()) { OptionalInstance(type) }
        }

        fun getAll() = optionals.values
    }

    fun getId() = "__ec_optional_${type.hashCode()}_t"
}