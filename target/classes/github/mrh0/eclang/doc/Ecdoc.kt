package github.mrh0.eclang.doc

import github.mrh0.eclang.ast.Loc
import java.io.File

/**
 * An ecdoc, `/** ... */`, as in Javadoc: a Markdown description, then block tags (`@param name text`,
 * `@return text`, ...). Types are never written in it: they come from the declaration.
 */
class Ecdoc(val location: Loc, val description: String, val tags: List<DocTag>) {
    fun tags(name: String) = tags.filter { it.name == name }
    fun tag(name: String) = tags.firstOrNull { it.name == name }

    val params get() = tags(PARAM)
    val returns get() = tag(RETURN)
    val throws get() = tags(THROWS)
    val see get() = tags(SEE)
    val since get() = tag(SINCE)
    val deprecated get() = tag(DEPRECATED)
    val authors get() = tags(AUTHOR)

    /** The first sentence of the description, for summaries. */
    val summary: String get() {
        val text = description.substringBefore("\n\n").replace(Regex("\\s*\n\\s*"), " ").trim()
        val end = Regex("[.!?](\\s|$)").find(text) ?: return text
        return text.substring(0, end.range.first + 1)
    }

    companion object {
        const val PARAM = "param"
        const val RETURN = "return"
        const val THROWS = "throws"
        const val SEE = "see"
        const val SINCE = "since"
        const val DEPRECATED = "deprecated"
        const val AUTHOR = "author"

        /** Tag names and the names they stand for (`@returns` is `@return`, `@exception` is `@throws`). */
        private val known = mapOf(
            PARAM to PARAM, RETURN to RETURN, "returns" to RETURN, THROWS to THROWS, "exception" to THROWS,
            SEE to SEE, SINCE to SINCE, DEPRECATED to DEPRECATED, AUTHOR to AUTHOR,
        )
        private val tagStart = Regex("^@([A-Za-z]\\w*)\\s*(.*)$")

        /**
         * Parses [raw], the text of an ecdoc (with its `/**` and `*/`) that starts at [file]:[line]:[column],
         * for a declaration that throws the types [thrown] (`@throws Name text` names one of them; otherwise the
         * whole text describes the error). Problems (an unknown tag, `@param` without a name) are added to [problems].
         */
        fun parse(raw: String, file: File?, line: Int, column: Int, problems: MutableList<DocProblem>, thrown: Collection<String> = listOf()): Ecdoc {
            val at = Loc(null, file, line, column)
            val lines = strip(raw)
            val description = mutableListOf<String>()
            val tags = mutableListOf<DocTag>()
            var current: Triple<String, Int, MutableList<String>>? = null // tag name, line, text
            fun finish() {
                val (name, l, text) = current ?: return
                val more = text.drop(1).joinToString("\n").trimIndent() // the lines after the tag's, without their indentation
                tags += tag(name, Loc(null, file, line + l, 0), (text.first() + "\n" + more).trimEnd(), problems, thrown)
                current = null
            }
            var fenced = false
            lines.forEachIndexed { i, text ->
                val match = if (fenced) null else tagStart.find(text.trimStart())
                if (text.trimStart().startsWith("```") || text.trimStart().startsWith("~~~")) fenced = !fenced
                if (match != null) {
                    finish()
                    current = Triple(match.groupValues[1], i, mutableListOf(match.groupValues[2]))
                } else (current?.third ?: description) += text
            }
            finish()
            return Ecdoc(at, description.joinToString("\n").trim('\n').trimEnd(), tags)
        }

        private fun tag(written: String, at: Loc, text: String, problems: MutableList<DocProblem>, thrown: Collection<String>): DocTag {
            val name = known[written] ?: run {
                problems += DocProblem(at, "Unknown ecdoc tag `@$written`. The tags are ${known.keys.joinToString { "`@$it`" }}. " +
                    "To start a line of the description with `@` (a pointer type, say), put it in backquotes: `` `@$written` ``.")
                return DocTag(written, at, null, text)
            }
            if (name != PARAM && name != THROWS) return DocTag(name, at, null, text.trim())
            // `@param name text`, and `@throws Type text` when the declaration throws Type.
            val arg = Regex("^(<\\w+>|[\\w.]+)").find(text)?.value?.takeIf { name == PARAM || it in thrown }
            if (name == PARAM && arg == null)
                problems += DocProblem(at, "`@param` needs the parameter's name first: `@param count how many to read`.")
            val rest = if (arg != null) text.substring(arg.length).trim() else text.trim()
            return DocTag(name, at, arg, rest)
        }

        /**
         * The lines of an ecdoc without `/**`, `*/` and the `*` that starts each line (and one space after
         * it). Without such stars, the lines lose their common indentation. Indentation beyond that is kept, for
         * Markdown code blocks.
         */
        private fun strip(raw: String): List<String> {
            val inner = raw.removePrefix("/**").removeSuffix("*/")
            val lines = inner.lines().map { it.trimEnd() }
            val first = lines.first().trimStart().let { if (it.all { c -> c == '*' }) "" else it }
            val rest = lines.drop(1).map { if (it.isNotBlank() && it.trim().all { c -> c == '*' }) "" else it }
            val starred = rest.filter { it.isNotBlank() }.all { it.trimStart().startsWith("*") }
            val body = if (starred) rest.map { it.trimStart().removePrefix("*").removePrefix(" ") }
                else {
                    val indent = rest.filter { it.isNotBlank() }.minOfOrNull { it.length - it.trimStart().length } ?: 0
                    rest.map { if (it.isBlank()) "" else it.substring(indent) }
                }
            return listOf(first) + body
        }
    }
}

/** A block tag: `@name arg text`. [arg] is the parameter of `@param` and the type of `@throws`, when written. */
class DocTag(val name: String, val location: Loc, val arg: String?, val text: String)

/** Something wrong with an ecdoc: a warning, as the comment is only documentation. */
class DocProblem(val location: Loc, val message: String)
