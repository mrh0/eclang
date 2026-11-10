package github.mrh0.eclang.util

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcError
import github.mrh0.ect2.Distinct
import github.mrh0.ect2.Prim
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * What follows the `:` in `"${x:>8.2f}"`: `[<|>][+][0][width][.precision][type]`, as in Python. It is checked against the
 * type of the value and becomes a printf conversion. Text is aligned left by default, numbers right.
 */
class FormatSpec private constructor(
    private val text: String, private val align: Char?, private val plus: Boolean, private val zero: Boolean,
    private val width: Int?, private val precision: Int?, private val kind: Char?,
) {
    /** The printf conversion and C arguments for a [type], given the conversion it has without a spec, [plain]. */
    fun apply(location: Loc, type: Type, plain: Pair<String, (String) -> String>): Pair<String, (String) -> String> {
        val t = if (type is Distinct) type.base else type
        fun bad(why: String): Nothing = throw EcError(location, "`:$text` can't format a $type: $why")
        val pad = (width?.toString() ?: "")
        val dot = precision?.let { ".$it" } ?: ""
        fun flags(left: Boolean) = (if (left) "-" else "") + (if (plus) "+" else "") + (if (zero) "0" else "")
        if (zero && align == '<') bad("`0` pads on the left, so it can't be used with `<`.")

        val isFloat = t === Types.Float || t === Types.Double
        val isWhole = t is Prim && t.isInteger && (t !== Types.Char || kind != null)
        if (isFloat) {
            if (kind != null && kind !in "feEgG") bad("a decimal number takes `f`, `e`, `E`, `g` or `G`. Convert it first for a whole number: `x as Long`.")
            return "%${flags(align == '<')}$pad$dot${kind ?: 'g'}" to plain.second
        }
        if (isWhole) {
            if (kind !in listOf(null, 'd', 'x', 'X', 'o')) bad("a whole number takes `d`, `x`, `X` or `o`." + if (kind in listOf('f', 'e', 'g')) " Convert it first for a decimal number: `x as Double`." else "")
            if (precision != null) bad("a whole number has no precision. Convert it first for decimals: `x as Double`.")
            val conversion = plain.first.removePrefix("%")
            val size = conversion.dropLast(1)
            val letter = if (kind == null || kind == 'd') (if (t === Types.Char) 'd' else conversion.last()) else kind
            if (plus && letter != 'd') bad("`+` is only for signed numbers shown in decimal.")
            return "%${flags(align == '<')}$pad$size$letter" to plain.second
        }
        // Text: Strings, CStrings, Bools, atoms and Chars.
        if (kind != null && kind != 's') bad("it is text, so the only type it takes is `s`.")
        if (plus || zero) bad("`+` and `0` are for numbers.")
        val left = align != '>'
        if (t === Types.Char) {
            if (precision != null) bad("a Char has no precision.")
            return "%${flags(left)}${pad}c" to plain.second
        }
        if (t === Types.Atom) bad("an atom is written with its `:`, which printf can't pad. Use `\${x}`.")
        if (t === Types.String) return "%${flags(left)}$pad.*s" to { a -> "(int)${precision?.let { "($a.len < $it ? $a.len : $it)" } ?: "$a.len"}, $a.data" }
        return plain.first.replace("%s", "%${flags(left)}$pad${dot}s") to plain.second
    }

    /** One `${...}` of a string, taken apart: the expression, the text `x=` for [label], and the format. */
    class Field(val expression: String, val label: String?, val spec: FormatSpec?)

    companion object {
        private val SPEC = Regex("""^(?:(.)?([<>^]))?(\+)?(0)?([1-9][0-9]*)?(?:\.([0-9]+))?([a-zA-Z])?$""")
        private const val HELP = "A format is `[<|>][+][0][width][.precision][type]`, e.g. `\${x:.2f}`, `\${n:>8}` or `\${n:08x}`."

        /** Splits the source inside `${...}`, at [location], into its parts: `x`, `x=`, `x:.2f` or `x=:.2f`. */
        fun split(location: Loc, source: String): Field {
            val colon = colon(source)
            val spec = colon?.let { parse(location, source.substring(it + 1)) }
            val written = if (colon != null) source.substring(0, colon) else source
            val expression = written.trimEnd()
            val isLabel = expression.endsWith("=") && expression.getOrNull(expression.length - 2) !in listOf('=', '!', '<', '>')
            if (!isLabel) return Field(expression, null, spec)
            return Field(expression.dropLast(1), written, spec)
        }

        /**
         * Where the format starts: the first `:` outside brackets and literals that comes right after a value. (An atom
         * in an expression follows an operator or a space: `${s == :ok}`.)
         */
        private fun colon(source: String): Int? {
            var depth = 0
            var i = 0
            while (i < source.length) {
                when (source[i]) {
                    '(', '[', '{' -> depth++
                    ')', ']', '}' -> depth--
                    '\\' -> i++
                    '"' -> { i++; while (i < source.length && source[i] != '"') { if (source[i] == '\\') i++; i++ } }
                    '\'' -> i += if (source.getOrNull(i + 1) == '\\') 3 else 2
                    ':' -> if (depth == 0 && i > 0 && followsValue(source, i)) return i
                }
                i++
            }
            return null
        }

        private fun followsValue(source: String, colon: Int): Boolean {
            val before = source[colon - 1]
            if (before == '=') return source.getOrNull(colon - 2).let { it != null && it !in "=!<>" }
            return before.isLetterOrDigit() || before in "_)]}\"'!?"
        }

        private fun parse(location: Loc, text: String): FormatSpec {
            val m = SPEC.find(text) ?: throw EcError(location, "`:$text` isn't a format. $HELP")
            val (fill, align, plus, zero, width, precision, kind) = m.destructured
            if (fill.isNotEmpty() || align == "^")
                throw EcError(location, "`:$text`: a fill character and centering (`^`) aren't supported, because C's printf has neither. Use `<`, `>`, or `0` to pad a number with zeros.")
            return FormatSpec(text, align.firstOrNull(), plus.isNotEmpty(), zero.isNotEmpty(), width.toIntOrNull(), precision.toIntOrNull(), kind.firstOrNull())
        }
    }
}
