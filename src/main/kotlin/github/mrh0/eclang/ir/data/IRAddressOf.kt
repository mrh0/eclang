package github.mrh0.eclang.ir.data

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

class IRAddressOf(location: Loc, val expr: IIR) : IR(location) {
    override fun toString() = "addrof($expr)"

    override fun isConstant() = expr.isConstant()

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put('&')
        expr.toC(sb, c)
    }
}

/** `addrof` of a literal that must outlive its C block: stores [value] in [storage], and points there. */
class IRAddressOfKept(location: Loc, val storage: String, val value: IIR) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put("($storage = ")
        value.toC(sb, c)
        sb.put(", &$storage)")
    }
}
