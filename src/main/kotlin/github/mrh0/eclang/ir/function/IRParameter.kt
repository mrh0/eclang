package github.mrh0.eclang.ir.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.eclang.types.EcType

class IRParameter(location: Loc, val name: String, val type: EcType) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        IRType(location, type).toC(sb, c)
        sb.put(' ')
        sb.put(name)
    }
}