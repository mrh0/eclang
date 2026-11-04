package github.mrh0.eclang.ast.token.loop

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.loop.IRLoopElse
import github.mrh0.ect2.Type

/**
 * Compiles a loop: [compileBody] inside the loop (for `break`/`continue`), [makeLoop] to build its IR, and the
 * optional [elseBody], which runs when the loop ends without `break` (like Python's `while ... else`).
 * [elseNarrowing] applies what is known in the else block, e.g. that the `while` condition is false.
 * [endless]: the loop only ends with `break` (`while true`), so without one the code after it is unreachable.
 */
internal fun compileLoop(
    cd: CompileData, location: Loc, hint: Type, elseBody: ITok?, elseNarrowing: () -> Unit = {}, endless: Boolean = false,
    compileBody: () -> IIR, makeLoop: (IIR) -> IIR,
): IIR {
    val body = cd.loop(elseBody != null, compileBody)
    val loop = makeLoop(body.value)
    if (endless && !body.breaks) cd.ctx().setReturned()
    if (elseBody == null) return loop
    cd.ctx().push()
    try {
        elseNarrowing()
        return IRLoopElse(location, loop, elseBody.process(cd, hint).second, body.label)
    } finally {
        cd.ctx().pop()
    }
}
