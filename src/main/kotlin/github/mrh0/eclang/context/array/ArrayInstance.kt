package github.mrh0.eclang.context.array

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.context.atom.AtomInstance
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.eclang.types.EcType

class ArrayInstance private constructor(val type: EcType) {
    companion object {
        private val arrays: MutableMap<Int, ArrayInstance> = mutableMapOf()

        fun get(type: EcType): ArrayInstance {
            return arrays.getOrPut(type.hashCode()) { ArrayInstance(type) }
        }

        fun getAll() = arrays.values
    }

    fun getId() = "__ec_array_${type.hashCode()}_t"

    fun toC(sb: CSourceBuilder, c: Context) {
        sb.put("typedef struct { long len; ")
        IRType(Loc.IDENTITY, type).toC(sb, c)
        sb.putLine("* data; } ${getId()};")
    }
}