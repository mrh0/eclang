package github.mrh0.eclang.context.tuple

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.util.Util

class TupleInstance private constructor(val types: Array<EcType>) {
    companion object {
        private val tuples: MutableMap<Int, TupleInstance> = mutableMapOf()

        fun get(types: Array<EcType>): TupleInstance {
            return tuples.getOrPut(types.hashCode()) { TupleInstance(types) }
        }

        fun getAll() = tuples.values
    }

    fun getId() = "__ec_tuple_${types.hashCode()}_t"

    fun toC(sb: CSourceBuilder, c: Context) {
        sb.put("typedef struct { ")
        types.forEachIndexed { index, type ->
            IRType(Loc.IDENTITY, type).toC(sb, c)
            sb.put(" ${Util.getTupleEntryName(index, types.size)}; ")
        }
        sb.putLine(" } ${getId()};")
    }
}