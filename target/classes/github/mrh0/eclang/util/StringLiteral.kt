package github.mrh0.eclang.util

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcError

/**
 * The text of a string literal as the program sees it: UTF-8 bytes, with escapes decoded. [size] is its length
 * in bytes (`"hé".len` is 3, `"a\tb".len` is 3), and [toC] writes a C literal holding exactly these bytes.
 */
class StringLiteral private constructor(private val bytes: ByteArray, private val fromEscape: BooleanArray) {
    val size get() = bytes.size

    /** Byte [i], from 0 to 255. */
    fun byteAt(i: Int): Int = bytes[i].toInt() and 0xFF

    /**
     * The C spelling, without the quotes. Characters written as themselves stay readable (including non-ASCII
     * ones, as UTF-8); other bytes become C escapes. `?` after `?` is escaped so it can't form a trigraph.
     */
    fun toC(): String {
        val out = StringBuilder()
        var i = 0
        while (i < bytes.size) {
            val b = bytes[i].toInt() and 0xFF
            if (b >= 0x80 && !fromEscape[i]) {
                val start = i
                while (i < bytes.size && bytes[i].toInt() and 0x80 != 0 && !fromEscape[i]) i++
                out.append(String(bytes, start, i - start, Charsets.UTF_8))
                continue
            }
            out.append(when {
                b == '"'.code -> "\\\""
                b == '\\'.code -> "\\\\"
                b == '\n'.code -> "\\n"
                b == '\t'.code -> "\\t"
                b == '\r'.code -> "\\r"
                b == '?'.code && out.endsWith("?") -> "\\?"
                b in 0x20..0x7E -> b.toChar().toString()
                else -> "\\" + b.toString(8).padStart(3, '0')
            })
            i++
        }
        return out.toString()
    }

    /** The parts of this text between its 0 bytes. */
    fun splitAtZero(): List<StringLiteral> {
        val parts = mutableListOf<StringLiteral>()
        var start = 0
        for (i in 0..bytes.size) if (i == bytes.size || bytes[i] == 0.toByte()) {
            parts += StringLiteral(bytes.copyOfRange(start, i), fromEscape.copyOfRange(start, i))
            start = i + 1
        }
        return parts
    }

    override fun toString() = String(bytes, Charsets.UTF_8)

    companion object {
        private val simple = mapOf('n' to '\n', 'r' to '\r', 't' to '\t', '0' to '\u0000', '\\' to '\\', '"' to '"', '\'' to '\'',
            'a' to '\u0007', 'b' to '\b', 'f' to '\u000C', 'v' to '\u000B', '$' to '$')

        /**
         * Splits [content] (the source text between the quotes) at each `${expr}`: the result alternates between
         * literal source text and expression source text, starting and ending with literal text (maybe empty).
         */
        /** Where the first `${` of [content] (the source text between the quotes) that has no closing `}` starts, or null. */
        fun unclosedInterpolation(content: String): Int? {
            var i = 0
            while (i < content.length) {
                when {
                    content[i] == '\\' -> i += 2
                    content.startsWith("\${", i) -> {
                        var depth = 1
                        var j = i + 2
                        while (j < content.length && depth > 0) {
                            if (content[j] == '{') depth++ else if (content[j] == '}') depth--
                            j++
                        }
                        if (depth > 0) return i
                        i = j
                    }
                    else -> i++
                }
            }
            return null
        }

        fun splitInterpolation(location: Loc, content: String): List<String> {
            val parts = mutableListOf<String>()
            val literal = StringBuilder()
            var i = 0
            while (i < content.length) {
                when {
                    content[i] == '\\' -> { literal.append(content, i, minOf(i + 2, content.length)); i += 2 }
                    content.startsWith("\${", i) -> {
                        var depth = 1
                        var j = i + 2
                        while (j < content.length && depth > 0) {
                            if (content[j] == '{') depth++ else if (content[j] == '}') depth--
                            j++
                        }
                        if (depth > 0) throw EcError(location, "`\${` in a string has no closing `}`. To write a dollar sign followed by `{`, escape it: `\\\${`.")
                        val expr = content.substring(i + 2, j - 1)
                        if (expr.isBlank()) throw EcError(location, "`\${}` in a string is empty; put an expression inside, e.g. `\${count}`.")
                        parts += literal.toString(); literal.clear()
                        parts += expr
                        i = j
                    }
                    else -> { literal.append(content[i]); i++ }
                }
            }
            parts += literal.toString()
            return parts
        }

        /** Decodes [content], the source text between the quotes of a literal at [location]. */
        /** [text] as it is, with no escapes. */
        fun of(text: String): StringLiteral = text.toByteArray(Charsets.UTF_8).let { StringLiteral(it, BooleanArray(it.size)) }

        fun parse(location: Loc, content: String): StringLiteral {
            val bytes = java.io.ByteArrayOutputStream()
            val fromEscape = ArrayList<Boolean>()
            fun add(b: Int, escape: Boolean) { bytes.write(b); fromEscape += escape }
            var i = 0
            while (i < content.length) {
                val ch = content[i]
                if (ch != '\\') {
                    val cp = content.codePointAt(i)
                    String(Character.toChars(cp)).toByteArray(Charsets.UTF_8).forEach { add(it.toInt() and 0xFF, false) }
                    i += Character.charCount(cp)
                    continue
                }
                val e = content.getOrNull(i + 1) ?: throw EcError(location, "A string can't end with a single `\\`; write `\\\\` for a backslash.")
                val decoded = simple[e]
                when {
                    decoded != null -> { add(decoded.code, true); i += 2 }
                    e == 'x' -> {
                        val hex = content.substring(i + 2, minOf(i + 4, content.length))
                        if (hex.length != 2 || !hex.all { it.isDigit() || it.lowercaseChar() in 'a'..'f' })
                            throw EcError(location, "`\\x` in a string needs two hex digits, e.g. `\\x41` for 'A'.")
                        add(hex.toInt(16), true)
                        i += 4
                    }
                    e == 'u' -> {
                        val braced = content.getOrNull(i + 2) == '{'
                        val end = if (braced) content.indexOf('}', i + 3) else minOf(i + 6, content.length)
                        val hex = when {
                            !braced -> content.substring(i + 2, end)
                            end > 0 -> content.substring(i + 3, end)
                            else -> ""
                        }
                        val cp = if (hex.isNotEmpty() && hex.length <= 6 && hex.all { it.isDigit() || it.lowercaseChar() in 'a'..'f' } && (braced || hex.length == 4)) hex.toInt(16) else -1
                        if (cp < 0 || cp > 0x10FFFF || cp in 0xD800..0xDFFF)
                            throw EcError(location, "`\\u` in a string needs a Unicode code point: four hex digits (`\\u00e9` for 'é') or up to six in braces (`\\u{1F600}`).")
                        // Printable characters stay readable in the C source; others become escapes.
                        val printable = !Character.isISOControl(cp) && !Character.isWhitespace(cp) && Character.getType(cp) != Character.FORMAT.toInt()
                        String(Character.toChars(cp)).toByteArray(Charsets.UTF_8).forEach { add(it.toInt() and 0xFF, !printable) }
                        i = if (braced) end + 1 else end
                    }
                    else -> throw EcError(location, "Unknown escape `\\$e` in a string. The escapes are \\n \\r \\t \\0 \\\\ \\\" \\' \\$ \\a \\b \\f \\v, \\xHH and \\uHHHH (or \\u{H...}); write `\\\\` for a backslash.")
                }
            }
            return StringLiteral(bytes.toByteArray(), fromEscape.toBooleanArray())
        }
    }
}
