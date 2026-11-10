package github.mrh0.eclang.ir.data

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.eclang.util.StringLiteral

/**
 * The elements of a `Char[N]` given as a String literal, inside the braces of an `IRCreateFixed`: `"abc"`, which C
 * pads with zeros. A literal of exactly N bytes has no room for C's 0, which gcc warns about, so it is written as
 * its Chars: `'a', 'b', 'c'`.
 */
class IRFixedText(location: Loc, private val value: StringLiteral, private val size: Long) : IRValue(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        if (value.size < size) {
            sb.put("\"${value.toC()}\"")
            return
        }
        sb.put((0 until value.size).joinToString(", ") { i -> char(value.byteAt(i)) })
    }

    private fun char(b: Int): String = when {
        b == '\''.code -> "'\\''"
        b == '\\'.code -> "'\\\\'"
        b in 0x20..0x7E -> "'${b.toChar()}'"
        else -> "'\\x${"%02x".format(b)}'"
    }

    override fun toString() = "fixedText($value)"
}
