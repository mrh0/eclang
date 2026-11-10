package github.mrh0.eclang.output.c

import github.mrh0.eclang.context.Context
import github.mrh0.eclang.context.function.Overload
import github.mrh0.eclang.ir.IIR
import github.mrh0.ect2.Type
import github.mrh0.ect2.TypeContext

class CSource(private val types: TypeContext, private val printers: Map<Type, Overload?>, private val library: Boolean = false, private val checkMemory: Boolean = false) {
    fun build(ir: IIR): String {
        val outputBuilder = CSourceBuilder(CTypes(types, printers, library).also { it.checkMemory = checkMemory })
        ir.toC(outputBuilder, Context.IDENTITY)
        return outputBuilder.toString()
    }
}
