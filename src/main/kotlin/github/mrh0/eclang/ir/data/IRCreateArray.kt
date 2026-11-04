package github.mrh0.eclang.ir.data

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.output.BlockScope
import github.mrh0.eclang.output.c.CSourceBuilder

class IRCreateArray(location: Loc, val arrayType: IRType, val type: IRType, val entries: List<IIR>) : IR(location) {
    override fun isConstant() = entries.all { it.isConstant() }

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put('(')
        arrayType.toC(sb, c)
        // An empty array (no extra arguments for `...rest: T`): C has no empty array literal.
        if (entries.isEmpty()) {
            sb.put("){0, NULL}")
            return
        }
        sb.put("){")
        sb.put(entries.size)
        sb.put(",(")
        type.toC(sb, c)
        sb.put("[]){")
        entries.forEachIndexed {
            index, it ->
            if (index != 0) sb.put(", ")
            it.toC(sb, c)
        }
        sb.put("}}")
    }
}