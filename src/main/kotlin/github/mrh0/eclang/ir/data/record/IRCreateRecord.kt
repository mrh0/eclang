package github.mrh0.eclang.ir.data.record

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.output.BlockScope
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.eclang.types.EcType

class IRCreateRecord(location: Loc, val name: String, val entries: List<IIR>) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put("(struct $name){")
        entries.forEachIndexed {
            index, it ->
            if (index != 0) sb.put(", ")
            it.toC(sb, c)
        }
        sb.put('}')
    }
}