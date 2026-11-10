package github.mrh0.eclang.ir.arithmetic

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.ir.IRConvert
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Prim

/**
 * `left op right` on whole numbers of [type], checked at runtime by a generated helper (`__ec_div_Int(a, b, "f.ec:3")`):
 * a division by zero, a shift by [countType] bits outside the type, or (with `--overflow-check`) a result that doesn't
 * fit stops the program with the line, where C would crash or have undefined behaviour.
 */
class IRCheckedArith(location: Loc, private val op: String, private val type: Prim, private val countType: Prim?, val left: IIR, val right: IIR) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put("${sb.types.checkedArith(op, type, countType)}(")
        left.toC(sb, c)
        sb.put(", ")
        right.toC(sb, c)
        sb.put(", ${IRConvert.where(location)})")
    }

    override fun toString() = "checked($left $op $right)"
}

/**
 * `target op= value` with a checked [op] (see [IRCheckedArith]): `{ T* p = &target; *p = helper(*p, value, at); }`,
 * so the target is evaluated once. With [noPointer], for what C has no pointer to: `target = helper(target, value, at);`.
 */
class IRCheckedCompound(
    location: Loc, private val target: IIR, private val op: String, private val type: Prim, private val countType: Prim?, private val value: IIR,
    private val temp: String, private val noPointer: Boolean = false,
) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        val t = sb.types.cName(type)
        sb.putStatement()
        if (noPointer) {
            target.toC(sb, c)
            sb.put(" = ${sb.types.checkedArith(op, type, countType)}(")
            target.toC(sb, c)
            sb.put(", ")
            value.toC(sb, c)
            sb.put(", ${IRConvert.where(location)})")
            sb.endStatement()
            return
        }
        sb.put("{ $t* $temp = &(")
        target.toC(sb, c)
        sb.put("); *$temp = ${sb.types.checkedArith(op, type, countType)}(*$temp, ")
        value.toC(sb, c)
        sb.put(", ${IRConvert.where(location)}); }")
        sb.endStatement()
    }

    override fun toString() = "checked($target $op= $value)"
}
