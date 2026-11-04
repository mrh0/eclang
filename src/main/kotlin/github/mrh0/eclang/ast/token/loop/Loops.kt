package github.mrh0.eclang.ast.token.loop

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.ir.IRLabel
import github.mrh0.eclang.ir.loop.IRLoopElse
import github.mrh0.ect2.Type

/** A loop, which `outer: while ...` gives a [label]. */
interface ILabeledLoop {
    var label: String?
}

/**
 * Compiles a loop: [compileBody] inside the loop (for `break`/`continue`), [makeLoop] to build its IR, and the
 * optional [elseBody], which runs when the loop ends without `break` (like Python's `while ... else`).
 * [elseNarrowing] applies what is known in the else block, e.g. that the `while` condition is false.
 * [endless]: the loop only ends with `break` (`while true`), so without one the code after it is unreachable.
 * [label]: its name, for `break outer` and `continue outer` in the loops inside it.
 */
internal fun compileLoop(
    cd: CompileData, location: Loc, hint: Type, elseBody: ITok?, elseNarrowing: () -> Unit = {}, endless: Boolean = false, label: String? = null,
    compileBody: () -> IIR, makeLoop: (IIR) -> IIR,
): IIR {
    val body = cd.loop(location, elseBody != null, label, compileBody)
    // `continue outer` jumps to the end of the body, past its deferred statements (it has already run them).
    val loop = makeLoop(if (body.nextLabel == null) body.value else IRBlock(location, listOf(body.value, IRLabel(location, body.nextLabel)), listOf()))
    if (endless && !body.breaks) cd.ctx().setReturned()
    if (elseBody == null) return if (body.label == null) loop else IRBlock(location, listOf(loop, IRLabel(location, body.label)), listOf())
    cd.ctx().push()
    try {
        elseNarrowing()
        return IRLoopElse(location, loop, elseBody.process(cd, hint).second, body.label)
    } finally {
        cd.ctx().pop()
    }
}
