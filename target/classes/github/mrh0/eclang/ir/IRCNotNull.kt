package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Type

/**
 * `((T)__ec_c_nn(value, "file:line", "name"))`: a pointer from C that its declaration says is never null: the result of
 * the C function [name], or with [field] the field [name] (`stat.name`) of a `declare rec`. C can't promise that, so a
 * NULL stops the program with a message instead of crashing later.
 */
class IRCNotNull(location: Loc, private val value: IIR, private val type: Type, private val name: String, private val field: Boolean = false) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put("((${sb.types.cName(type)})${if (field) "__ec_c_nn_field" else "__ec_c_nn"}((void*)(")
        value.toC(sb, c)
        sb.put("), ${IRConvert.where(location)}, \"$name\"))")
    }

    override fun toString() = "notNull($value)"
}

/** A pointer from C that eclang treats as never null (a pointer, CString or C array, not `T?`), so it's checked: see [IRCNotNull]. */
fun mayBeNullInC(t: Type) = t is github.mrh0.ect2.Pointer || t === github.mrh0.ect2.Types.CString || (t is github.mrh0.ect2.ArrayType && t.isC)
