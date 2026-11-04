package github.mrh0.eclang.output.c

import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.ect2.TypeContext

class CSource(private val types: TypeContext) {
    fun build(ir: IIR): String {
        val outputBuilder = CSourceBuilder(CTypes(types))
        ir.toC(outputBuilder, Context.IDENTITY)
        return outputBuilder.toString()
    }
}
