package github.mrh0.eclang.error

import github.mrh0.eclang.types.EcType

class CatchableInstance private constructor(val types: Array<EcType>) {
    companion object {
        private val catchables: MutableMap<Int, CatchableInstance> = mutableMapOf()

        fun get(types: Array<EcType>): CatchableInstance {
            return catchables.getOrPut(types.hashCode()) { CatchableInstance(types) }
        }

        fun getAll() = catchables.values
    }

    fun getId() = "__ec_catchable_${types.hashCode()}_t"
}