package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

/** A use of a top-level `const` (a C `#define`), by name. [value] is what it stands for. */
class IRConstantRef(location: Loc, val name: String, val value: IIR) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put(name)
    }

    override fun isConstant() = true

    override fun toString() = name
}
