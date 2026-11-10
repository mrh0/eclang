package github.mrh0.eclang.ir.arithmetic

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.ir.IRConvert
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Prim

/**
 * [value], of the type [type] of bit-field [field] with [width] bits, checked at runtime to fit it
 * (`__ec_bits_UInt_3(v, "mode", "f.ec:3")`): C would cut it to the width.
 */
class IRCheckedBits(location: Loc, private val value: IIR, private val type: Prim, private val width: Int, private val field: String) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put("${sb.types.checkedBits(type, width)}(")
        value.toC(sb, c)
        sb.put(", \"$field\", ${IRConvert.where(location)})")
    }

    override fun toString() = "checkedBits($value: $field)"
}

/**
 * `target op= value` on bit-field [field]: `target = check((T)(target op value))`, with the operation itself checked when
 * [checked] (see [IRCheckedArith]). There is no pointer to a bit-field, so the target is written twice; it has no calls.
 */
class IRBitFieldCompound(
    location: Loc, private val target: IIR, private val op: String, private val type: Prim, private val countType: Prim?, private val value: IIR,
    private val width: Int, private val field: String, private val checked: Boolean,
) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.putStatement()
        target.toC(sb, c)
        sb.put(" = ${sb.types.checkedBits(type, width)}(")
        if (checked) sb.put("${sb.types.checkedArith(op, type, countType)}(") else sb.put("(${sb.types.cName(type)})(")
        target.toC(sb, c)
        sb.put(if (checked) ", " else " $op (")
        value.toC(sb, c)
        if (checked) sb.put(", ${IRConvert.where(location)})") else sb.put("))")
        sb.put(", \"$field\", ${IRConvert.where(location)})")
        sb.endStatement()
    }

    override fun toString() = "checkedBits($target $op= $value)"
}
