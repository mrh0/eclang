package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.BlockScope
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Result

/**
 * `try f()`: calls f, and if it failed, runs the deferred statements and returns the error from the current
 * function (converted to its error type).
 * ```
 * R tmp = f();
 * if (tmp.code != 0) { <deferred>; return (Outer){.code = 1, .error = tmp.error}; }
 * ```
 * With [copyInto] (the C name of a pool), the error is deep-copied into that pool first: inside a `pool do` block
 * it may be in the block's pool, which the deferred statements free. [outerOf] gives the result type of the function: in a
 * lambda that infers its `throws`, it is only known after its whole body is compiled.
 */
class IRTryPropagate(
    location: Loc, val temp: String, val type: Result, val call: IIR, private val outerOf: () -> Result, val deferred: List<IIR>,
    val copyInto: () -> String? = { null },
) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        val outer = outerOf()
        val copyInto = copyInto()
        sb.putStatement("${sb.types.cName(type)} $temp = ")
        call.toC(sb, c)
        sb.endStatement()
        sb.putStatement("if ($temp.code != 0) ")
        sb.pushScope(BlockScope())
        val converted = sb.types.convert(type.error, outer.error, "$temp.error", IRConvert.where(location))
        val error = if (copyInto == null) converted else {
            sb.putStatement("${sb.types.cName(outer.error)} ${temp}_error = ${sb.types.clone(outer.error, converted, copyInto)}")
            sb.endStatement()
            "${temp}_error"
        }
        deferred.forEach { it.toC(sb, c) }
        sb.putStatement("return ((${sb.types.cName(outer)}){.code = 1, .error = $error})")
        sb.endStatement()
        sb.popScope()
    }

    override fun toString() = "TryPropagate($call)"
}
