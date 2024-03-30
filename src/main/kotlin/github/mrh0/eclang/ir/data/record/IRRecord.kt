package github.mrh0.eclang.ir.data.record

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.output.BlockScope
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.eclang.types.EcType

class IRRecord(location: Loc, val name: String, val entries: List<Pair<String, IRType>>) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.putStatement("struct $name ")
        sb.pushScope(BlockScope())
        entries.forEach {
            sb.putStatement()
            it.second.toC(sb, c)
            sb.put(' ')
            sb.put(it.first)
            sb.endStatement()
        }
        sb.popScope(true)
    }
}