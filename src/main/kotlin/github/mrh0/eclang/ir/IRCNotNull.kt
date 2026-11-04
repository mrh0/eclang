package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Type

/**
 * `((T)__ec_c_nn(call, "file:line", "name"))`: the pointer result of the C function [function], which its declaration
 * says is never null. C can't promise that, so a NULL stops the program with a message instead of crashing later.
 */
class IRCNotNull(location: Loc, private val call: IIR, private val type: Type, private val function: String) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put("((${sb.types.cName(type)})__ec_c_nn((void*)(")
        call.toC(sb, c)
        sb.put("), ${IRConvert.where(location)}, \"$function\"))")
    }

    override fun toString() = "notNull($call)"
}
