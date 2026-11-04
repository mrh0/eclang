package github.mrh0.eclang.ir.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Type

/** `T name`. eclang stops parameters from being reassigned; the C stays plain so `addrof` works. */
class IRParameter(location: Loc, val name: String, val type: Type, private val def: IIR?) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put("${sb.types.cName(type)} $name")
    }

    fun getDefaultValue() = def
    fun hasDefaultValue() = def != null

    override fun toString(): String {
        return "IRParameter($name: $type)"
    }
}
