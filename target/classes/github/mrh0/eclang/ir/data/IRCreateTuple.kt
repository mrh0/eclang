package github.mrh0.eclang.ir.data

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Tuple

/** `((__ec_tuple_Int_Int){.first = a, .second = b})`. */
class IRCreateTuple(location: Loc, val tuple: Tuple, val values: List<IIR>) : IR(location) {
    override fun isConstant() = values.all { it.isConstant() }

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put("((${sb.types.cName(tuple)}){")
        values.forEachIndexed { i, it ->
            if (i != 0) sb.put(", ")
            sb.put(".${sb.types.tupleField(i)} = ")
            it.toC(sb, c)
        }
        sb.put("})")
    }

    override fun toString() = "(${values.joinToString()})"
}
