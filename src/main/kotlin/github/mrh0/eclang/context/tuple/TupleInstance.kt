package github.mrh0.eclang.context.tuple

import github.mrh0.eclang.types.EcType

class TupleInstance private constructor(val types: Array<EcType>) {
    companion object {
        private val tuples: MutableMap<Int, TupleInstance> = mutableMapOf()

        fun get(types: Array<EcType>): TupleInstance {
            return tuples.getOrPut(types.hashCode()) { TupleInstance(types) }
        }

        fun getAll() = tuples.values
    }

    fun getId() = "__ec_tuple_${types.hashCode()}_t"
}