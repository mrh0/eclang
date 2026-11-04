package github.mrh0.eclang.doc

/**
 * Renders [FileDocs] as Markdown: per file, its overview, then its types, values and functions, each with its
 * signature, description and tags. Undocumented declarations are listed with their signature; `priv` ones only
 * with [includePrivate]. `{@link name}` and `@see name` link to a declaration of the documented files when there is
 * one (`name`, `Type.field` or `module.name`, `#` working as `.`).
 */
class Markdown(private val files: List<FileDocs>, private val includePrivate: Boolean = false) {
    /** Anchor ids: `name`, `module.name` and `Type.member` (`module.Type.member`). */
    private val ids = buildSet {
        for (f in files) for (item in f.items) {
            val id = qualified(f, item.name)
            add(id)
            item.members.forEach { add("$id.${it.name}") }
        }
    }
    private val anchored = mutableSetOf<String>()
    private val out = StringBuilder()
    private lateinit var current: FileDocs

    fun render(): String {
        for (f in files) {
            current = f
            file(f)
        }
        return out.toString().trimEnd() + "\n"
    }

    private fun qualified(f: FileDocs, name: String) = f.module?.let { "$it.$name" } ?: name

    private fun line(text: String = "") { out.append(text).append('\n') }

    private fun file(f: FileDocs) {
        line(if (f.module != null) "# Module `${f.module}`" else "# `${f.file.name}`")
        line()
        f.overview?.let { doc ->
            if (doc.description.isNotEmpty()) { line(inline(doc.description)); line() }
            footer(doc)
        }
        val items = f.items.filter { includePrivate || !it.isPrivate }
        for (title in listOf("Types", "Values", "Functions")) {
            val section = items.filter { it.kind.title == title }
            if (section.isEmpty()) continue
            line("## $title")
            line()
            section.forEach { item(f, it) }
        }
    }

    private fun item(f: FileDocs, item: DocItem) {
        val id = qualified(f, item.name)
        line("### ${anchor(id)}`${item.name}`")
        line()
        line("```ec")
        line(item.signature)
        line("```")
        line()
        val doc = item.doc
        val notes = listOfNotNull(
            if (item.isPrivate) "Private to its module (`priv`)." else null,
            item.cName?.let { "Binds the C function `$it`." },
        )
        doc?.deprecated?.let { line("> **Deprecated.** ${inline(it.text)}".trimEnd()); line() }
        if (doc != null && doc.description.isNotEmpty()) { line(inline(doc.description)); line() }
        if (notes.isNotEmpty()) { line(notes.joinToString(" ") { "*$it*" }); line() }
        if (doc != null) {
            if (doc.params.any { it.arg != null }) {
                line("**Parameters**")
                line()
                doc.params.filter { it.arg != null }.forEach { line(bullet("`${it.arg}`", it.text)) }
                line()
            }
            doc.returns?.let { line("**Returns** ${inline(it.text)}".trimEnd()); line() }
            if (doc.throws.isNotEmpty()) {
                line("**Throws**")
                line()
                doc.throws.forEach { line(bullet("`${it.arg ?: thrown(item.signature)}`", it.text)) }
                line()
            }
        }
        if (item.members.isNotEmpty()) {
            line("**${item.members.first().kind.title.let { t -> if (item.members.any { it.kind.title != t }) "Members" else t }}**")
            line()
            for (m in item.members) {
                val text = listOfNotNull(m.doc?.deprecated?.let { "**Deprecated.** ${it.text}" }, m.doc?.description).joinToString(" ")
                line(bullet("${anchor("$id.${m.name}")}`${m.signature}`", text))
            }
            line()
        }
        doc?.let { footer(it) }
    }

    /**
     * An HTML anchor for the first heading or list item with this [id] (an overload's later ones get none). It is
     * inline: an `<a>` tag on a line of its own would start an HTML block and swallow the heading after it.
     */
    private fun anchor(id: String) = if (anchored.add(id)) "<a id=\"$id\"></a>" else ""

    /** What `throws` after the return type names, for a `@throws` that doesn't name the type. */
    private fun thrown(signature: String) = signature.substringAfterLast(" throws ", "Error")

    /** `@since`, `@author` and `@see` lines. */
    private fun footer(doc: Ecdoc) {
        val lines = listOfNotNull(
            doc.since?.let { "**Since** ${inline(it.text)}" },
            doc.authors.takeIf { it.isNotEmpty() }?.let { a -> "**Author** ${a.joinToString(", ") { inline(it.text) }}" },
            doc.see.takeIf { it.isNotEmpty() }?.let { s -> "**See also** ${s.joinToString(", ") { see(it.text) }}" },
        )
        if (lines.isEmpty()) return
        line(lines.joinToString("  \n"))
        line()
    }

    /** `- head — text`, with the text's further lines indented to stay in the item. */
    private fun bullet(head: String, text: String): String {
        val body = inline(text).trim()
        return if (body.isEmpty()) "- $head" else "- $head — ${body.replace("\n", "\n  ")}"
    }

    /** `@see name`, `@see name label`, `@see "text"`, `@see <a ...>` or `@see [label](url)`. */
    private fun see(text: String): String {
        if (text.startsWith("\"")) return text.trim('"')
        if (text.startsWith("<") || text.startsWith("[") || text.startsWith("http")) return if (text.startsWith("http")) "<$text>" else text
        val ref = text.substringBefore(' ')
        val label = text.substringAfter(' ', "").trim()
        return link(ref, label.ifEmpty { null }, code = label.isEmpty())
    }

    /** Replaces `{@link name label}`, `{@linkplain name label}` and `{@code text}`. */
    private fun inline(text: String): String = Regex("\\{@(link|linkplain|code)\\s+([^}]*)}").replace(text) { m ->
        val body = m.groupValues[2].trim()
        when (m.groupValues[1]) {
            "code" -> "`$body`"
            else -> {
                val ref = body.substringBefore(' ')
                val label = body.substringAfter(' ', "").trim().ifEmpty { null }
                link(ref, label, code = m.groupValues[1] == "link" && label == null)
            }
        }
    }

    private fun link(ref: String, label: String?, code: Boolean): String {
        val name = ref.replace('#', '.').substringBefore('(').trimStart('.')
        val shown = label ?: ref.substringBefore('(').trimStart('#')
        val text = if (code) "`$shown`" else shown
        val id = listOfNotNull(current.module?.let { "$it.$name" }, name).firstOrNull { it in ids } ?: return text
        return "[$text](#$id)"
    }
}
