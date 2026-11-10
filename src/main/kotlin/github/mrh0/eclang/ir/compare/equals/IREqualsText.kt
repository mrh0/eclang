package github.mrh0.eclang.ir.compare.equals

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.FixedArray

/** `a == s` (or `!=` when [negated]) of a `Char[N]` [fixed] and a String [text]: the Chars up to the first 0 against the String's bytes. */
class IREqualsText(location: Loc, private val fixed: IIR, private val text: IIR, private val type: FixedArray, private val negated: Boolean) : IR(location) {
    override fun toString() = "($fixed ${if (negated) "!=" else "=="} $text)"

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put("${if (negated) "!" else ""}${sb.types.fixedTextEquals(type)}(")
        fixed.toC(sb, c)
        sb.put(", ")
        text.toC(sb, c)
        sb.put(")")
    }
}
