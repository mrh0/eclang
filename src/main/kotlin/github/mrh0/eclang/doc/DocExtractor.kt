package github.mrh0.eclang.doc

import github.mrh0.eclang.antlr.EclangParser.*
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.util.Util
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.misc.Interval
import org.antlr.v4.runtime.tree.TerminalNode
import java.io.File

/** What a documented declaration is. [title] is the plural heading it is listed under. */
enum class DocKind(val title: String) {
    FUNCTION("Functions"), C_FUNCTION("Functions"),
    RECORD("Types"), C_RECORD("Types"), C_STRUCT("Types"), INTERFACE("Types"), TYPE("Types"), ENUM("Types"),
    CONSTANT("Values"), VALUE("Values"), VARIABLE("Values"), C_VALUE("Values"),
    FIELD("Fields"), METHOD("Methods"), ENUM_VALUE("Values"),
}

/**
 * A declaration, with its ecdoc (or null) and its [signature] as written (a function's is rebuilt as
 * `fn name(params): R throws E`, `declare fn` included). [members] are a record's fields, an interface's
 * fields and methods, and an enum's values. [cName] is the C name of a `declare`d function.
 */
class DocItem(
    val kind: DocKind,
    val name: String,
    val signature: String,
    val location: Loc,
    val doc: Ecdoc?,
    val isPrivate: Boolean = false,
    val members: List<DocItem> = listOf(),
    val cName: String? = null,
)

/**
 * The documentation of one file: the declarations in source order, the module it belongs to (null for the top
 * level), the [overview] (the ecdoc before `module "name"`, or before the first `use`) and the problems found.
 */
class FileDocs(val file: File, val module: String?, val overview: Ecdoc?, val items: List<DocItem>, val problems: List<DocProblem>)

/**
 * Attaches ecdocs to the declarations of a parsed file. The lexer sets every ecdoc aside, keyed by
 * the start offset of the token after it ([docs]); a declaration takes the one before its first token. Comments
 * that end up attached to nothing (inside a function body, say) are reported, as are tags that don't fit the
 * declaration (`@param` naming no parameter, `@return` on a function that returns nothing).
 */
class DocExtractor(private val file: File, private val docs: Map<Int, Token>, private val strays: List<Token>) {
    private val problems = mutableListOf<DocProblem>()
    private val used = mutableSetOf<Int>()

    fun extract(program: ProgramContext): FileDocs {
        val items = mutableListOf<DocItem>()
        var module: String? = null
        var overview: Ecdoc? = null
        var seenUse = false
        for (child in program.children.orEmpty()) when (child) {
            is FuncContext -> items += function(child, child.start, false)
            is PrivateFunctionContext -> items += function(child.func(), child.start, true)
            is ExportDeclarationContext -> items += function(child.func(), child.start, false, exported = true)
            is PrivateGlobalContext -> global(child.global(), child.start, true)?.let { items += it }
            is GlobalNamespaceContext -> {
                module = Util.getStringContent(child.name.text)
                doc(child.start, "the module")?.let { overview = it }
            }
            is GlobalContext -> global(child, child.start, false)?.let { items += it }
            is UseContext -> if (!seenUse) {
                seenUse = true
                // An ecdoc before the first `use` describes the file, unless `module` has one.
                doc(child.start, "the description of the file")?.let { if (overview == null) overview = it }
            }
        }
        for ((start, token) in docs) if (start !in used) stray(token)
        strays.forEach { stray(it) }
        return FileDocs(file, module, overview, items, problems.sortedWith(compareBy({ it.location.getLine() }, { it.location.getCharPos() })))
    }

    private fun stray(token: Token) {
        problems += DocProblem(Loc(token, file), "This ecdoc doesn't document anything. Put it right before a declaration " +
            "(`fn`, `rec`, `inter`, `val`, a record field, ...), before `module` or the first `use` to describe the file, " +
            "or write `/*` instead of `/**` to make it an ordinary comment.")
    }

    /**
     * The ecdoc before [start], parsed for a declaration of the kind [what] (named in the problems) that
     * throws [thrown].
     */
    private fun doc(start: Token, what: String, thrown: List<String> = listOf()): Ecdoc? {
        val token = docs[start.startIndex] ?: return null
        used += start.startIndex
        val doc = Ecdoc.parse(token.text, file, token.line, token.charPositionInLine, problems, thrown)
        // `@param <T>` documents a type parameter, which a generic record has too.
        for (tag in doc.tags) if ((tag.name == Ecdoc.PARAM && !(what == "a record" && tag.arg?.startsWith("<") == true)) || tag.name == Ecdoc.RETURN || tag.name == Ecdoc.THROWS)
            problems += DocProblem(tag.location, "`@${tag.name}` documents a function, but this is $what.${
                if (what == "a record" || what == "an interface") " Document each field with an ecdoc right before it." else ""}")
        return doc
    }

    private fun function(f: FuncContext, start: Token, isPrivate: Boolean, exported: Boolean = false): DocItem {
        val (name, params, written) = when (f) {
            is FunctionDeclareContext -> Triple(f.name, f.params, f.returnType to f.throwing)
            is FunctionBlockContext -> Triple(f.name, f.params, f.returnType to f.throwing)
            is FunctionInlineContext -> Triple(f.name, f.params, f.returnType to f.throwing)
            else -> error("unknown function form ${f.javaClass.simpleName}")
        }
        // `fn f(): T throws E` parses either way: as `throws` after the type or as a type `T throws E`.
        val (returns, throws) = written.first.let { r -> if (written.second == null && r is TypeThrowsContext) r.left to r.throwing else written }
        val thrown = throws?.let { text(it).split('|').map { t -> t.trim().removePrefix("(").removeSuffix(")").trim() } }.orEmpty()
        val signature = (if (exported) "export " else "") + (if (f.getChild(0).text == "inline") "inline " else "") + "fn ${name.text}(${params.joinToString(", ") { text(it) }})" +
            (returns?.let { ": ${text(it)}" } ?: "") + (throws?.let { " throws ${text(it)}" } ?: "")

        val token = docs[start.startIndex]
        val doc = token?.let {
            used += start.startIndex
            Ecdoc.parse(it.text, file, it.line, it.charPositionInLine, problems, thrown)
        }
        if (doc != null) {
            val names = params.mapNotNull { paramName(it) }
            val generics = params.flatMap { p -> Regex("(?<![\\w>])<(\\w+)>").findAll(text(p)).map { "<${it.groupValues[1]}>" } }.distinct()
            val fn = "'${name.text}'"
            val documented = mutableSetOf<String>()
            for (tag in doc.params) {
                val arg = tag.arg ?: continue
                if (!documented.add(arg)) problems += DocProblem(tag.location, "`@param $arg` is written twice; merge the two into one.")
                else if (arg !in names && arg !in generics) problems += DocProblem(tag.location,
                    "`@param $arg`: $fn has no parameter '$arg'.${CompileData.suggestion(arg, names + generics).ifEmpty {
                        if (names.isEmpty()) " It has no parameters." else " Its parameters are ${(names + generics).joinToString { "'$it'" }}." }}")
            }
            if (doc.tags(Ecdoc.RETURN).size > 1) problems += DocProblem(doc.tags(Ecdoc.RETURN)[1].location, "`@return` is written twice; merge the two into one.")
            if (returns == null) doc.returns?.let {
                problems += DocProblem(it.location, "`@return`: $fn returns nothing. Remove the tag, or give $fn a return type (`fn ${name.text}(...): Int`).")
            }
            if (throws == null) doc.throws.firstOrNull()?.let {
                problems += DocProblem(it.location, "`@throws`: $fn doesn't throw. Remove the tag, or add the error type after the return type (`: Int throws Error`).")
            }
        }
        val cName = if (f is FunctionDeclareContext) Util.getStringContent(f.externalName?.text) ?: f.name.text else null
        return DocItem(if (f is FunctionDeclareContext) DocKind.C_FUNCTION else DocKind.FUNCTION, name.text, signature, Loc(name, file), doc, isPrivate, cName = cName)
    }

    private fun paramName(p: ParameterContext): String? = when (p) {
        is ParameterVariableContext -> paramName(p.parameter())
        is ParameterConstContext -> paramName(p.parameter())
        is ParameterVolatileContext -> paramName(p.parameter())
        else -> p.getToken(NAME, 0)?.text
    }

    private fun global(g: GlobalContext, start: Token, isPrivate: Boolean): DocItem? {
        fun item(kind: DocKind, name: Token, what: String, signature: String = text(g), members: List<DocItem> = listOf()) =
            DocItem(kind, name.text, signature, Loc(name, file), doc(start, what), isPrivate, members)
        fun value(kind: DocKind, what: String) = g.getToken(NAME, 0).symbol.let { item(kind, it, "$what '${it.text}'") }
        fun fields(names: List<Token>, types: List<TypeContext>) = names.mapIndexed { i, n ->
            DocItem(DocKind.FIELD, n.text, "${n.text}: ${text(types[i])}", Loc(n, file), doc(n, "a record field"))
        }
        return when (g) {
            is GlobalDefineContext, is GlobalDefineTypedContext -> value(DocKind.VARIABLE, "the variable")
            is GlobalDefineVolatileContext, is GlobalDefineVolatileTypedContext -> value(DocKind.VARIABLE, "the variable")
            is GlobalDefineConstContext, is GlobalDefineConstTypedContext -> value(DocKind.VALUE, "the value")
            is GlobalConstantContext -> value(DocKind.CONSTANT, "the constant")
            is GlobalDeclareDefineContext -> item(DocKind.C_VALUE, g.name, "the C variable '${g.name.text}'")
            is GlobalDeclareConstContext -> item(DocKind.C_VALUE, g.name, "the C value '${g.name.text}'")
            is GlobalTypeDefineContext -> g.NAME().symbol.let { item(DocKind.TYPE, it, "the type '${it.text}'") }
            is GlobalTypeAliasContext -> item(DocKind.TYPE, g.name, "the type '${g.name.text}'")
            is GlobalEnumDeclareContext -> item(DocKind.ENUM, g.name, "the enum '${g.name.text}'", header(g), g.atoms.mapIndexed { i, a ->
                DocItem(DocKind.ENUM_VALUE, a.text, "${a.text} = ${g.constants[i].text}", Loc(a, file), doc(a, "an enum value"))
            })
            is GlobalRecordDefineContext -> item(DocKind.RECORD, g.name, "a record", header(g), g.fields.map { field ->
                DocItem(DocKind.FIELD, field.name.text, text(field), Loc(field.name, file), doc(field.start, "a record field"))
            })
            is GlobalUnionDefineContext -> item(DocKind.RECORD, g.name, "a record", header(g), g.fields.map { field ->
                DocItem(DocKind.FIELD, field.name.text, text(field), Loc(field.name, file), doc(field.start, "a record field"))
            })
            is GlobalUnionDeclareDefineContext -> item(DocKind.C_STRUCT, g.name, "a record", header(g), fields(g.names, g.types))
            is GlobalUnionDeclareContext -> item(DocKind.C_STRUCT, g.name, "a record", header(g))
            is GlobalRecordDeclareDefineContext -> item(DocKind.C_RECORD, g.name, "a record", header(g), fields(g.names, g.types))
            is GlobalRecordDeclareContext -> item(DocKind.C_RECORD, g.name, "a record", header(g))
            is GlobalStructDeclareDefineContext -> item(DocKind.C_STRUCT, g.name, "a record", header(g), fields(g.names, g.types))
            is GlobalStructDeclareContext -> item(DocKind.C_STRUCT, g.name, "a record", header(g))
            is GlobalInterfaceDefineContext -> item(DocKind.INTERFACE, g.name, "an interface", header(g), g.members.map { m ->
                val name = m.getToken(NAME, 0).symbol
                DocItem(if (m is InterfaceMethodContext) DocKind.METHOD else DocKind.FIELD, name.text, text(m), Loc(name, file), doc(m.start, "an interface member"))
            })
            else -> null // `unit` (unsupported)
        }
    }

    /** The source text of [ctx], with runs of spaces and line breaks made one space (a newline ends a declaration anyway). */
    private fun text(ctx: ParserRuleContext): String {
        val stop = ctx.children.orEmpty().lastOrNull { !isLayout(it) }?.let { (it as? ParserRuleContext)?.stop ?: (it as TerminalNode).symbol } ?: ctx.stop
        return source(ctx.start, stop)
    }

    /** The source text of [ctx] up to its `as` (`rec Point extends Shape`). */
    private fun header(ctx: ParserRuleContext): String {
        val before = ctx.children.takeWhile { !(it is TerminalNode && (it.text == "as" || isLayout(it))) }
        val last = before.last().let { (it as? ParserRuleContext)?.stop ?: (it as TerminalNode).symbol }
        return source(ctx.start, last)
    }

    private fun isLayout(node: Any): Boolean = node is TerminalNode && node.symbol.type.let { it == NL || it == INDENT || it == DEDENT }

    private fun source(start: Token, stop: Token): String =
        start.inputStream.getText(Interval(start.startIndex, stop.stopIndex)).trim().replace(Regex("\\s*\n\\s*"), " ")
}
