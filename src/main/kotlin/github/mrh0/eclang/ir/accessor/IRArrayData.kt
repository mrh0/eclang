package github.mrh0.eclang.ir.accessor

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Type

/**
 * `x.data` of a String or `T[]` ([data] reads the field), as a [type] (a `T[]c`). An empty array may have no memory
 * (`var a: Int[]` is `{0, NULL}`), but a `T[]c` is never null, so `__ec_data` gives a pointer to a static object then.
 */
class IRArrayData(location: Loc, private val data: IIR, private val type: Type) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put("((${sb.types.cName(type)})__ec_data(")
        data.toC(sb, c)
        sb.put("))")
    }

    override fun toString() = "data($data)"
}
