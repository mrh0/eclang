package github.mrh0.eclang.output.c

import github.mrh0.eclang.context.Context
import github.mrh0.eclang.context.atom.AtomInstance
import github.mrh0.eclang.ir.IIR

class CSource {
    fun build(ir: IIR): String {
        val outputBuilder = CSourceBuilder();


        ir.toC(outputBuilder, Context.IDENTITY)
        return outputBuilder.toString()
    }
}