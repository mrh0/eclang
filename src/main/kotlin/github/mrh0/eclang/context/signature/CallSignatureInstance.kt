package github.mrh0.eclang.context.signature

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.eclang.types.EcType

class CallSignatureInstance private constructor(val argTypes: Array<EcType>, val returnType: EcType) {
    companion object {
        private val arrays: MutableMap<Int, CallSignatureInstance> = mutableMapOf()

        fun get(argTypes: Array<EcType>, returnType: EcType): CallSignatureInstance {
            return arrays.getOrPut(31 * returnType.hashCode() + 31 * argTypes.contentHashCode()) { CallSignatureInstance(argTypes, returnType) }
        }

        fun getAll() = arrays.values
    }

    private val hash = (31 * returnType.hashCode() + 31 * argTypes.contentHashCode()).toUInt()

    fun getId() = "__ec_call_sig_${hash}_t"

    fun toC(sb: CSourceBuilder, c: Context) {
        sb.put("typedef ")
        IRType(Loc.IDENTITY, returnType).toC(sb, c)
        sb.put("(*${getId()})(")
        argTypes.forEachIndexed { index, it ->
            if (index != 0) sb.put(",")
            IRType(Loc.IDENTITY, it).toC(sb, c)
        }
        sb.putLine(");")
    }
}