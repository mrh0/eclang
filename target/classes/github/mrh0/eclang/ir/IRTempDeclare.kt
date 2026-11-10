package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** `T name;` (`T name[length];`), a generated temporary that is set later. [type] may only be known after the code using it is compiled. */
class IRTempDeclare(location: Loc, val name: String, val type: () -> Type, val length: Int? = null) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        if (type() === Types.Never) return // every path that would set it leaves first
        sb.putStatement("${sb.types.cName(type())} $name${length?.let { "[$it]" } ?: ""}")
        sb.endStatement()
    }

    override fun toString() = "Temp($name)"
}
