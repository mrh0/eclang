package github.mrh0.eclang.context.result

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.eclang.types.EcType

class ResultInstance private constructor(val returnType: EcType, val errorType: EcType) {
    companion object {
        private val arrays: MutableMap<Int, ResultInstance> = mutableMapOf()

        fun get(returnType: EcType, errorType: EcType): ResultInstance {
            return arrays.getOrPut(31 * returnType.hashCode() + 31 * errorType.hashCode()) { ResultInstance(returnType, errorType) }
        }

        fun getAll() = arrays.values
    }

    val hash = (31 * returnType.hashCode() + 31 * errorType.hashCode()).toUInt()

    fun getId() = "__ec_result_${hash}_t"

    fun toC(sb: CSourceBuilder, c: Context) {
        sb.put("typedef struct { int code; union { ")
        IRType(Loc.IDENTITY, returnType).toC(sb, c)
        sb.putLine(" value; ")
        IRType(Loc.IDENTITY, errorType).toC(sb, c)
        sb.putLine(" error; } } ${getId()};")
    }
}