package github.mrh0.eclang.ir.data

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.FixedArray

/** An array literal of type [type], a `T[N]`: `((W){{1, 2, 3}})`. */
class IRCreateFixed(location: Loc, private val type: FixedArray, private val entries: List<IIR>) : IR(location) {
    override fun isConstant() = entries.all { it.isConstant() }

    /** `{1, 2, 3}`, which is also how a record literal sets a field that is a C array. */
    fun braces(sb: CSourceBuilder, c: Context) {
        sb.put('{')
        entries.forEachIndexed { i, it ->
            if (i != 0) sb.put(", ")
            it.toC(sb, c)
        }
        sb.put('}')
    }

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put("((${sb.types.cName(type)}){")
        braces(sb, c)
        sb.put("})")
    }

    override fun toString() = "$type$entries"
}
