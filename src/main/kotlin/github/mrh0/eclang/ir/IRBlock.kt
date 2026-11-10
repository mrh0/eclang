package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.BlockScope
import github.mrh0.eclang.output.c.CSourceBuilder

/** A block's statements, then its deferred statements. [diverges]: the block always leaves early (ret, throw, ...). */
class IRBlock(location: Loc, val statements: List<IIR>, val deferred: List<IIR>, val diverges: Boolean = false) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        statements.forEach { lineOf(sb, it); it.toC(sb, c) }
        deferred.forEach { it.toC(sb, c) }
    }

    companion object {
        /**
         * With `--check-memory`, a `#line` before the statement [ir], so that what AddressSanitizer reports (from the debug
         * information) names the eclang file and line rather than the C.
         */
        fun lineOf(sb: CSourceBuilder, ir: IIR) {
            if (!sb.types.checkMemory) return
            val location = (ir as? IR)?.location ?: return
            val file = location.file ?: return
            if (location.getLine() <= 0) return
            // Only between statements: a statement's code may also be written in the middle of a line.
            if (sb.sb.isNotEmpty() && sb.sb.last() != '\n') return
            sb.putLine("#line ${location.getLine()} \"${file.path.replace("\\", "\\\\").replace("\"", "\\\"")}\"")
        }
    }

    override fun toString(): String {
        return "IRBlock($statements)"
    }
}

/** [inner] in C braces, for a name that is scoped more narrowly than the block around it: `when val v = ...`. */
class IRScoped(location: Loc, private val inner: IIR) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.putStatement()
        sb.pushScope(BlockScope())
        inner.toC(sb, c)
        sb.popScope()
    }

    override fun toString() = "IRScoped($inner)"
}
