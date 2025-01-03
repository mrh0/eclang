package github.mrh0.eclang.ir.data.record

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.output.BlockScope
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.eclang.types.EcType

class IRHere(location: Loc) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put("(location_t){")
        sb.put("${location.getLine()}, ${location.getCharPos()}, ${location.getFilename()}")
        sb.put('}')
    }
}