package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.NonValueTok
import github.mrh0.eclang.ast.token.TBlock
import github.mrh0.eclang.ast.token.statement.Assertions
import github.mrh0.eclang.ast.token.statement.TDebug
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * `requires condition` or `ensures condition` after a function's signature, with an optional message, as [Assertions] checks it.
 * A `requires` is checked when the function starts, and reported at the call (see Functions.contractCall); the body then knows
 * that it holds (`requires p != null` narrows p). An `ensures` is checked at each `ret` and at the end, with `result` the value
 * returned, and reported there. With [debug] (`debug requires`), only a debug build checks it (see TDebug).
 * [sources] has the text of the condition's parts.
 */
class Contract(val location: Loc, val requires: Boolean, val condition: ITok, val message: ITok?, val source: String, val sources: Map<ITok, String>, val debug: Boolean) {
    val keyword get() = if (requires) "requires" else "ensures"

    /** The check, reported at [at] (a C expression), as a statement to compile where it runs. */
    fun check(name: String, at: String): ITok {
        val header = if (requires) "requirement of $name failed" else "guarantee of $name failed"
        val check = Check(location, this, header, at)
        return if (debug) TDebug(location, TBlock(location, listOf(check))) else check
    }

    private class Check(location: Loc, private val contract: Contract, private val header: String, private val at: String) : NonValueTok(location) {
        override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> = Types.None to IRBlock(location,
            Assertions.compile(cd, location, contract.condition, contract.source, contract.sources, contract.message, contract.keyword, header, at), listOf())

        override fun toString() = "${contract.keyword}(${contract.condition})"
    }

    override fun toString() = "$keyword ${source}"
}
