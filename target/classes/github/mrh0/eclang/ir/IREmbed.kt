package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

/**
 * A bundled C source (`use "core:vendor/x.c"`), written into the generated file as it is, so that a program still builds
 * from one C file with the usual flags. It shares that file with the program's own names: see src/main/resources/vendor/README.md.
 */
class IREmbed(location: Loc, val path: String, private val source: String) : IR(location) {
    override fun toString(): String {
        return "embed($path)"
    }

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.putLine(source.trimEnd())
    }

    override fun isConstant(): Boolean = true
}
