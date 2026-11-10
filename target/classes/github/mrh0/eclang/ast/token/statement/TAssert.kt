package github.mrh0.eclang.ast.token.statement

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.NonValueTok
import github.mrh0.eclang.ast.token.data.TBoolean
import github.mrh0.eclang.ast.unnested
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.ir.IRConvert
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * `assert condition` or `assert condition, message`: stops the program when condition is false, with a report naming the line,
 * the function, the condition and the values it compared (see [Assertions]); [message] (a String, made only then) comes first.
 * The code after it sees what condition proves: after `assert x != null`, x is not null. `assert false` never comes back, like
 * `panic`. [sources] has the text of the condition's parts, for the report.
 */
class TAssert(location: Loc, private val condition: ITok, private val message: ITok?, private val source: String, private val sources: Map<ITok, String>) : NonValueTok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val name = cd.function?.name
        val header = if (name != null) "assertion failed in $name" else "assertion failed"
        val at = IRConvert.where(location)
        // `assert false, "unreachable"`: a panic, after which nothing runs.
        if ((condition.unnested() as? TBoolean)?.value == false) {
            val ir = Assertions.fail(cd, location, message, header, at)
            cd.ctx().setReturned()
            return Types.None to ir
        }
        return Types.None to IRBlock(location, Assertions.compile(cd, location, condition, source, sources, message, "assert", header, at), listOf())
    }

    override fun toString() = "TAssert($condition)"
}
