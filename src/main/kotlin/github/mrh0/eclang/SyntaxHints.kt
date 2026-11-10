package github.mrh0.eclang

import github.mrh0.eclang.antlr.EclangParser
import org.antlr.v4.runtime.Token

/**
 * Turns ANTLR's syntax errors ("no viable alternative at input 'fnmain():Intindent'") into messages that say what is
 * wrong and how to fix it. [explain] tries a table of common mistakes in order; the first that matches wins, and
 * anything else gets a tidied version of ANTLR's message.
 */
object SyntaxHints {
    /** A syntax error at [line] (1-based) and [column] (0-based) of [lines]. [token]: the offending token (null for the lexer). */
    class Problem(val lines: List<String>, val line: Int, val column: Int, val token: Token?, val antlr: String) {
        val source: String get() = lines.getOrElse(line - 1) { "" }
        val code: String get() = source.trim()
        /** The first word of the line: `fn`, `if`, `val`, ... */
        val keyword: String get() = Regex("^[A-Za-z_]\\w*").find(code)?.value ?: ""
        /** The source from the error onwards. */
        val rest: String get() = source.drop(column)
        /** The source from just before the error (`a++` fails at its second `+`). */
        val around: String get() = source.drop(maxOf(0, column - 1))
        val type: Int get() = token?.type ?: Token.INVALID_TYPE
        /** The offending token is the end of a line (or of the file). */
        val atLineEnd: Boolean get() = type == EclangParser.NL || type == Token.EOF || token?.text?.startsWith("\n") == true || token?.text?.startsWith("\r") == true
        /** What ANTLR expected instead: `expecting {'as', 'extends'}` or `expecting NL`. */
        val expected: List<String> get() {
            val list = Regex("expecting \\{(.*)}$").find(antlr)?.groupValues?.get(1) ?: Regex("expecting (\\S+)$").find(antlr)?.groupValues?.get(1) ?: return listOf()
            return list.split(", ").map { it.trim().removeSurrounding("'") }
        }
        /** The character the lexer couldn't read, for "token recognition error at: ';'". */
        val badText: String? get() = Regex("token recognition error at: '(.*)'$", RegexOption.DOT_MATCHES_ALL).find(antlr)?.groupValues?.get(1)
        fun indentOf(line: Int) = lines.getOrElse(line - 1) { "" }.let { it.length - it.trimStart(' ').length }
        /** The same problem at the end of the last line with code before this one, for an error the parser only finds there. */
        fun lineBefore(): Problem? {
            val before = (line - 1 downTo 1).firstOrNull { lines.getOrElse(it - 1) { "" }.isNotBlank() } ?: return null
            return Problem(lines, before, lines[before - 1].length, token, antlr)
        }
    }

    /** Where to report a problem, and what to say. */
    class Hint(val line: Int, val column: Int, val message: String)

    /** `for (a, b) in ...`, which isn't the C-style `for` it starts like. */
    private val destructuringFor = Regex("^for\\s*\\([\\w\\s,()]*\\)\\s*in\\b")

    private val blockKeywords = setOf("fn", "if", "else", "while", "for", "inline", "defer")
    private val declarationKeywords = "`fn`, `val`, `var`, `const`, `rec`, `inter`, `use`, `module` or `declare`"

    /** Words a declaration can start with that are no keywords, so the parser reads any name there as one and fails after it. */
    private val declarationWords = setOf("packed", "align", "union", "export", "resource", "sync", "module")

    /** A line at the top level starting with a name that starts no declaration: `io.log(1)`. */
    private fun startsTopLevelStatement(p: Problem) = p.column > 0 && p.indentOf(p.line) == 0 && p.keyword.isNotEmpty() && p.keyword !in declarationWords && p.keyword !in reservedWords

    private val reservedWords = EclangParser.VOCABULARY.let { v -> (1..v.maxTokenType).mapNotNull { v.getLiteralName(it)?.removeSurrounding("'") } }.toSet()

    private fun rule(check: (Problem) -> Hint?) = check

    private fun at(p: Problem, message: String) = Hint(p.line, p.column, message)

    /** The table of common mistakes, most specific first. Each returns null when it doesn't apply. */
    private val rules: List<(Problem) -> Hint?> = listOf(
        // Tabs: the lexer only counts spaces as indentation, so a tab-indented line looks unindented.
        rule { p ->
            val tabLine = p.lines.take(p.line + 1).indexOfFirst { l -> l.isNotBlank() && '\t' in l.takeWhile { it == ' ' || it == '\t' } }
            if (tabLine < 0) null
            else Hint(tabLine + 1, 0, "This line is indented with a tab. eclang only counts spaces as indentation, so the blocks can't be read. " +
                "Indent with spaces (4 per level); most editors can convert tabs to spaces.")
        },
        // Characters the lexer can't read.
        rule { p ->
            when (val bad = p.badText) {
                null -> null
                ";" -> at(p, "eclang doesn't end statements with `;`. Remove it: a new line ends a statement.")
                else -> when {
                    bad.startsWith("\"") -> at(p, "This string has no closing `\"`. A string must end on the line it starts on.")
                    bad.startsWith("'") -> {
                        val text = Regex("^'([^']*)'?").find(p.rest)?.groupValues?.get(1) ?: bad.drop(1)
                        at(p, "Single quotes hold one character (`'a'`). Write text in double quotes: `\"$text\"`.")
                    }
                    else -> at(p, "eclang doesn't use the character `${bad.trim()}` here.")
                }
            }
        },
        // A line that ends a block header without `do` (or `as` for records and interfaces).
        rule { p ->
            if (p.type != EclangParser.INDENT) return@rule null
            when (p.keyword) {
                in blockKeywords ->
                    if (p.code.endsWith(":")) at(p, "A block starts with `do` in eclang, not `:`. Write `${p.code.dropLast(1).trimEnd()} do`.")
                    else at(p, "The indented block below needs a `do` at the end of this line: `${p.code} do`.")
                "rec", "inter" -> at(p, "The fields go in an indented block after `as`: `${p.code} as`.")
                else -> null
            }
        },
        rule { p ->
            if (p.keyword == "rec" && p.expected.contains("as")) at(p, "A record's fields follow `as`, not `${p.token?.text}`: `${p.keyword} Name as`, then one `name: Type` per indented line.")
            else null
        },
        // An unexpected indented line: deeper than its block, or not lined up with any block when going back out.
        rule { p ->
            if (p.type != EclangParser.INDENT) return@rule null
            val next = p.line + 1
            val indent = p.indentOf(next)
            if (indent > p.indentOf(p.line)) Hint(next, indent, "This line is indented more than the line before it, but that line doesn't start a block " +
                "(a block follows a line ending in `do`). Indent it like the line before.")
            else {
                // The indentation of the blocks this line could belong to, from the innermost out.
                val levels = (p.line downTo 1).map { p.indentOf(it) to p.lines[it - 1] }.filter { it.second.isNotBlank() }.map { it.first }
                    .fold(listOf<Int>()) { acc, i -> if (acc.isEmpty() || i < acc.last()) acc + i else acc }
                Hint(next, indent, "This line is indented by $indent spaces, which doesn't line up with any block around it " +
                    "(they are indented by ${levels.joinToString(", ")}). Indent it like the other lines of its block.")
            }
        },
        rule { p ->
            if ("expecting INDENT" in p.antlr) at(p, "The block after this line is missing. Indent its statements by 4 more spaces than this line, " +
                "or write `pass` (indented) for a block that does nothing.")
            else null
        },
        rule { p ->
            if (p.token?.text == "{" && p.keyword in blockKeywords) at(p, "Blocks are indented, not written in `{ }`. End the line with `do` and indent the body by 4 spaces.")
            else null
        },
        // Keywords from other languages. `return 0` reads as a word before a statement (`debug x`), which fails at the next line.
        rule { r ->
            val p = if (r.keyword.isEmpty()) r.lineBefore() ?: r else r
            when (p.keyword) {
                "return" -> at(p, "`return` is written `ret` in eclang: `${p.code.replaceFirst("return", "ret")}`.")
                "let" -> at(p, "Declare a value with `val` (it can't change) or `var` (it can): `${p.code.replaceFirst("let", "val")}`.")
                "func", "def", "function", "fun" -> Hint(p.line, 0, "Functions are declared with `fn`: `${p.code.replaceFirst(p.keyword, "fn")}`.")
                "elif" -> at(p, "`elif` is written `else if` in eclang.")
                "switch", "match", "case", "when" -> at(p, "eclang has no `${p.keyword}`. Use `if ... do`, `else if ... do` and `else`.")
                else -> null
            }
        },
        rule { p -> if (Regex("^for\\s*\\(").containsMatchIn(p.code) && !destructuringFor.containsMatchIn(p.code)) at(p, "eclang has no C-style `for`. Loop over a range with `for i in 0..<10 do`, or use `while cond do`.") else null },
        // `val (a, b) = ...` is a statement, not a declaration.
        rule { p ->
            if (p.token?.text == "(" && p.keyword in listOf("val", "var") && p.source.startsWith(p.keyword)) at(p, "`${p.keyword} (a, b) = ...` only works inside a function. At the top level, name the tuple (`${p.keyword} t = ...`) and read its parts with `t.first`, `t.second`.")
            else null
        },
        // A C-style declaration: `Int a = 1`.
        rule { p ->
            Regex("^([A-Z]\\w*\\??)\\s+([a-z_]\\w*)\\s*=\\s*(.*)$").find(p.code)?.let { m ->
                val (type, name, value) = m.destructured
                at(p, "Variables are declared with the type after the name: `val $name: $type = $value` (or `var` to change it later).")
            }
        },
        rule { p ->
            Regex("^fn\\s+\\w+\\s*\\(.*(?<!\\w)([a-z_]\\w*)\\s+([A-Z@\\[]\\w*)").find(p.code)?.let { m ->
                at(p, "Parameters are written `name: Type`: `${m.groupValues[1]}: ${m.groupValues[2]}`.")
            }
        },
        // Operators from other languages.
        rule { p ->
            when {
                p.around.startsWith("++") || p.around.startsWith("--") -> at(p, "eclang has no `${p.around.take(2)}`. Write `x ${p.around[0]}= 1`.")
                p.rest.startsWith("->") -> at(p, "The return type follows a colon: `fn name(): Int do`.")
                p.token?.text == "=" && p.keyword in listOf("if", "while", "else", "assert") -> at(p, "`=` assigns a value. To compare, write `==`.")
                p.token?.text == "?" && ':' in p.rest -> at(p, "eclang has no `cond ? a : b`. Write `if cond do a else b`.")
                p.token?.text == ":" && p.keyword in blockKeywords && p.rest.trim() == ":" -> at(p, "A block starts with `do` in eclang, not `:`. Write `${p.code.dropLast(1).trimEnd()} do`.")
                else -> null
            }
        },
        // Brackets.
        rule { p ->
            val closers = listOf(")", "]", "]c", "}")
            if (!p.atLineEnd || (p.expected.none { it in closers } && !p.antlr.startsWith("missing"))) return@rule null
            val open = unclosed(p.source) ?: return@rule null
            at(p, "A `$open` on this line isn't closed. Add the `${CLOSE.getValue(open)}` before the end of the line.")
        },
        rule { p ->
            val text = p.token?.text ?: return@rule null
            if (text !in listOf(")", "]", "}") || !p.antlr.startsWith("extraneous")) return@rule null
            at(p, "This `$text` has no matching `${CLOSE.entries.first { it.value == text }.key}`. Remove it, or add the opening one.")
        },
        rule { p ->
            if (p.atLineEnd && p.expected.containsAll(listOf("NAME", "INT"))) {
                val last = p.source.trimEnd().takeLastWhile { !it.isLetterOrDigit() && it != ' ' && it != ')' && it != ']' && it != '"' }
                at(p, "The line ends in the middle of an expression${if (last.isNotEmpty()) " (after `$last`)" else ""}. Finish it on this line.")
            } else null
        },
        // Statements at the top level of a file.
        rule { p ->
            if (p.expected.containsAll(listOf("<EOF>", "fn")) || startsTopLevelStatement(p)) Hint(p.line, 0, "Only declarations ($declarationKeywords) can be at the top level of a file. " +
                "Put statements inside a function, such as `fn main(): Int do`.")
            else null
        },
    )

    private val CLOSE = mapOf("(" to ")", "[" to "]", "{" to "}")

    /** The innermost bracket of [line] that isn't closed (outside strings), or null. */
    private fun unclosed(line: String): String? {
        val stack = ArrayDeque<String>()
        var inString: Char? = null
        var i = 0
        while (i < line.length) {
            val c = line[i]
            when {
                inString != null -> if (c == '\\') i++ else if (c == inString) inString = null
                c == '"' || c == '\'' -> inString = c
                c == '/' && line.getOrNull(i + 1) == '/' -> break
                c.toString() in CLOSE -> stack.addLast(c.toString())
                c.toString() in CLOSE.values -> if (stack.lastOrNull()?.let { CLOSE[it] } == c.toString()) stack.removeLast()
            }
            i++
        }
        return stack.lastOrNull()
    }

    fun explain(p: Problem): Hint = rules.firstNotNullOfOrNull { it(p) } ?: at(p, fallback(p))

    /** ANTLR's message without the parts that don't help: its glued-together input, and long lists of expected tokens. */
    private fun fallback(p: Problem): String {
        val near = when {
            p.type == Token.EOF -> " at the end of the file"
            p.type == EclangParser.INDENT -> " at an indented line"
            p.type == EclangParser.DEDENT -> " where a block ends"
            p.atLineEnd -> " at the end of the line"
            p.token != null -> " near '${p.token.text.trim()}'"
            else -> ""
        }
        val expected = p.expected.filter { it != "NL" }.map { if (it.all { c -> c.isUpperCase() }) it.lowercase() else "`$it`" }
        val what = when {
            expected.isNotEmpty() && expected.size <= 4 -> "Expected ${expected.joinToString(" or ")} here."
            else -> "This isn't valid eclang code. Check the line against the examples in the README."
        }
        return "Syntax error$near. $what"
    }
}
