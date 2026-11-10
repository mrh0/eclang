package github.mrh0.eclang

import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.Module
import github.mrh0.eclang.context.function.Overload
import github.mrh0.eclang.context.state.IVar
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.util.Util
import github.mrh0.ect2.*
import java.io.File

/**
 * Completion for an editor: `eclang check`'s `complete` request (see [Checker], ls/vscode). The word at the cursor is replaced
 * by the name [PROBE] ([prepare]) and the file is compiled. Where the compiler meets that name, as a value ([TNamed]), a field
 * or method (`x.name`, `x.name(...)`), a name of a module (`io.name`) or a type, it calls one of the functions below, which list
 * what fits there from what the compiler knows at that point: the locals with their narrowed types, the type expected there,
 * the receiver's type, what is private. That stops the compilation ([Hit]).
 *
 * Items are ranked (smaller first): what has the expected type, then what converts to it, then the rest. Code that is never
 * compiled (a generic function nobody calls) never meets the probe, and nor does a file whose declarations have errors, as
 * function bodies are compiled after them: the answer is then `"found":false`.
 */
class Completion(private val atom: Boolean) {
    /** One thing to offer. [params]: for a function, how many arguments it takes at most (after the receiver), else -1. */
    class Item(val label: String, val kind: String, val detail: String, val rank: Int, val module: String? = null, val params: Int = -1)

    /** What was found at the probe: [context] is name, member, module, type or atom. */
    class Found(val context: String, val expected: Type?, val receiver: Type?, val items: List<Item>)

    /** Thrown at the probe, to leave the compilation with what it found. */
    class Hit(val found: Found) : RuntimeException(null, null, false, false)

    companion object {
        /** The name that stands in for the word at the cursor. The `__ec_` prefix is reserved, so no program has it. */
        const val PROBE = "__ec_complete"

        /** A variable to hold the probe, in the last way [prepare] tries: `val ec_complete_value_ = x.__ec_complete`. */
        private const val HOLDER = "ec_complete_value_"

        // Base ranks, before what fits the expected type moves things up (see [fit]).
        private const val LOCAL = 0
        private const val FIELD = 0
        private const val METHOD = 1
        private const val GLOBAL = 2
        private const val FUNCTION = 3
        private const val TYPE = 5
        private const val MODULE = 6

        /** How much lower what C declares is ranked (`declare val`, `declare fn`), as eclang code usually has a function of its own for it. */
        private const val C_DECLARED = 2

        /** Whether [name] is something the compiler made, which code can't name. */
        private fun hidden(name: String) = name.startsWith("__")

        // -----------------------------------------------------------------------------------------------------------
        // Where the compiler meets the probe.

        /** The probe as a value, where a [hint] is expected. */
        fun name(cd: CompileData, hint: Type): Nothing {
            val expected = hint.takeIf { it !== Types.None }
            if (cd.completion?.atom == true) throw Hit(Found("atom", expected, null, atoms(cd, expected, 0)))
            val items = ArrayList<Item>()
            for ((name, v) in cd.visibleLocals()) if (!hidden(name)) items += variable(cd, name, v, LOCAL, expected)
            for (m in cd.visibleModules()) for (v in m.globals.stack.getVars()) {
                val name = v.getName()
                if (!hidden(name) && (m === cd.module || name !in m.privateGlobals) && items.none { it.label == name }) items += variable(cd, name, v, GLOBAL, expected, m)
            }
            items += functions(cd, cd.functions.visibleOverloads(), expected, receiver = false)
            items += atoms(cd, expected, LOCAL).filter { it.rank == LOCAL }
            items += types(cd, listOf(cd.module.name, "", "core").distinct(), expected)
            items += cd.allModules().map { Item(it.name, "module", "module \"${it.name}\"", MODULE) }
            throw Hit(Found("name", expected, null, merge(items)))
        }

        /** The probe as `x.name` or `x.name(...)`, x being a [receiver]. */
        fun member(cd: CompileData, receiver: Type, hint: Type): Nothing {
            val expected = hint.takeIf { it !== Types.None }
            val items = ArrayList<Item>()
            val plain = if (receiver is Distinct) receiver.base else receiver
            val target = if (plain is Pointer) plain.target else plain
            when {
                target is Declared -> target.fields.forEach { (name, type) -> items += Item(name, "field", type.toString(), rank(FIELD, cd, type, expected)) }
                target === Types.String || target is ArrayType && !target.isC || target is FixedArray -> {
                    val element = when (target) { is ArrayType -> target.element; is FixedArray -> target.element; else -> Types.Char }
                    items += Item("len", "field", Types.Size.toString(), rank(FIELD, cd, Types.Size, expected))
                    items += Item("data", "field", cd.types.cArray(element).toString(), rank(FIELD, cd, cd.types.cArray(element), expected))
                }
                target is Tuple -> target.elements.forEachIndexed { i, t -> items += Item(Util.getTupleEntryName(i, 0), "field", t.toString(), rank(FIELD, cd, t, expected)) }
            }
            (target as? Interface)?.let { i ->
                cd.interfaceMethods[i]?.forEach { m ->
                    items += Item(m.name, "method", "fn ${m.name}(${m.params.joinToString(", ")})${if (m.returns === Types.None) "" else ": ${m.returns}"}",
                        rank(METHOD, cd, m.returns, expected), params = m.params.size)
                }
            }
            val methods = cd.functions.methodsFor(receiver)
            items += functions(cd, methods.map { it.first }, expected, receiver = true) { o -> methods.first { it.first === o }.second }
            throw Hit(Found("member", expected, receiver, merge(items)))
        }

        /** The probe as `m.name`, m being a module. */
        fun module(cd: CompileData, m: Module, hint: Type): Nothing {
            val expected = hint.takeIf { it !== Types.None }
            val items = ArrayList<Item>()
            for (v in m.globals.stack.getVars()) {
                val name = v.getName()
                if (!hidden(name) && (m === cd.module || name !in m.privateGlobals)) items += variable(cd, name, v, GLOBAL, expected, m)
            }
            items += functions(cd, cd.functions.visibleOverloads(m), expected, receiver = false)
            items += types(cd, listOf(m.name), expected)
            throw Hit(Found("module", expected, null, merge(items)))
        }

        /** The probe as a type, or as `m.Name` when [qualifier] is the module m. */
        fun type(cd: CompileData, qualifier: String?): Nothing {
            val items = ArrayList<Item>()
            if (qualifier != null) cd.findModule(qualifier)?.let { items += types(cd, listOf(it.name), null) }
            else {
                cd.genericContext?.keys?.forEach { items += Item(it, "typeParameter", "type parameter", LOCAL) }
                items += types(cd, listOf(cd.module.name, "", "core").distinct(), null)
                items += cd.allModules().map { Item(it.name, "module", "module \"${it.name}\"", MODULE) }
            }
            throw Hit(Found("type", null, null, merge(items)))
        }

        // -----------------------------------------------------------------------------------------------------------
        // Items.

        /**
         * How well a [type] fits the [expected] one: 0 when it is one (or nothing is expected), 1 when it converts to one,
         * 3 when it doesn't. A generic type may fit, as a call decides it.
         */
        private fun fit(cd: CompileData, type: Type, expected: Type?): Int = when {
            expected == null -> 0
            type === Types.Never -> 2 // `panic(...)` fits anywhere, but is seldom what is meant
            Overload.containsTypeParam(type) -> 1
            else -> when (cd.types.cost(type, expected)) { 0 -> 0; TypeContext.NO -> 3; else -> 1 }
        }

        private fun rank(base: Int, cd: CompileData, type: Type, expected: Type?) = fit(cd, type, expected) * 10 + base

        private fun variable(cd: CompileData, name: String, v: IVar, base: Int, expected: Type?, m: Module? = null): Item {
            val kind = when {
                v.kind() == "a parameter" -> "parameter"
                v.canAssign() -> "variable"
                base == LOCAL -> "value"
                else -> "constant"
            }
            val c = if (v is github.mrh0.eclang.context.state.DeclaredConstant) C_DECLARED else 0
            return Item(name, kind, v.getType().toString(), rank(base + c, cd, v.getType(), expected), m?.name?.takeIf { it.isNotEmpty() })
        }

        /**
         * One item per function name of [overloads], ranked by its best overload: what its result fits, and with a [receiver]
         * (`x.name(...)`) what it costs to pass x ([receiverCost]). Its detail is the signature of that overload.
         */
        private fun functions(cd: CompileData, overloads: List<Overload>, expected: Type?, receiver: Boolean, receiverCost: (Overload) -> Int = { 0 }): List<Item> =
            overloads.filter { !hidden(it.name) && !it.namedOnly }.groupBy { it.module to it.name }.map { (key, list) ->
                fun rankOf(o: Overload) = rank((if (receiver) METHOD + minOf(receiverCost(o), 1) else FUNCTION) + (if (o.isExternal) C_DECLARED else 0), cd, if (expected != null && o.throws != null) o.returns else cd.functions.resultType(o), expected)
                val best = list.minBy(::rankOf)
                val shown = best.params.filter { !it.isCaller }.drop(if (receiver) 1 else 0)
                val varArg = best.varArg?.let { if (it.isCVarArg) "..." else "...${it.name}: ${it.type}" }
                val signature = "fn ${best.name}(${(shown.map { p -> (if (p.isMut) if (p.isOut) "out " else "mut " else "") + "${p.name}: ${if (p.isMut) (p.type as? Pointer)?.target ?: p.type else p.type}" } + listOfNotNull(varArg)).joinToString(", ")})" +
                    (if (best.returns === Types.None) "" else ": ${best.returns}") + (best.throws?.let { " throws $it" } ?: "") +
                    (if (list.size > 1) "  (+${list.size - 1} overload${if (list.size > 2) "s" else ""})" else "")
                val most = list.maxOf { o -> o.params.count { !it.isCaller } - (if (receiver) 1 else 0) + (if (o.varArg != null) 1 else 0) }
                Item(key.second, if (receiver) "method" else "function", signature, rankOf(best), key.first.name.takeIf { it.isNotEmpty() }, most.coerceAtLeast(0))
            }

        /** The atoms of [expected] first, then the others the program has used so far. */
        private fun atoms(cd: CompileData, expected: Type?, base: Int): List<Item> {
            // The atoms of the program so far, so `:x` written before a function that uses it is compiled is missing.
            val wanted = atomsOf(expected)
            val all = wanted + cd.atoms.getAll().map { it.label }.filter { it !in wanted }.sorted()
            return all.map { Item(":$it", "atom", expected?.takeIf { _ -> it in wanted }?.toString() ?: "atom", if (it in wanted) base else base + 5) }
        }

        private fun atomsOf(t: Type?): List<String> = when (t) {
            is AtomLiteral -> listOf(t.name)
            is Union -> t.members.flatMap { atomsOf(it) }.distinct()
            is Distinct -> atomsOf(t.base)
            else -> listOf()
        }

        /** The types named in [namespaces] that code here may use. */
        private fun types(cd: CompileData, namespaces: List<String>, expected: Type?): List<Item> = namespaces.flatMap { ns ->
            val owner = if (ns == "core") null else cd.moduleNamed(ns)
            cd.types.namedIn(ns).filter { (name, _) -> !hidden(name) && (owner == null || owner === cd.module || name !in owner.privateTypes) }.map { (name, t) ->
                val kind = when {
                    t is Interface -> "interface"
                    t is Record || t is GenericRecord -> "record"
                    atomsOf(t).isNotEmpty() -> "enum"
                    else -> "type"
                }
                Item(name, kind, if (t.toString() == name) kind else t.toString(), if (expected == null || t is GenericRecord) TYPE else rank(TYPE, cd, t, expected), ns.takeIf { it.isNotEmpty() && it != "core" })
            }
        }

        /** One item per label and kind, the best ranked; the best first. */
        private fun merge(items: List<Item>): List<Item> =
            items.groupBy { it.label to it.kind }.values.map { same -> same.minBy { it.rank } }.sortedWith(compareBy({ it.rank }, { it.label }))

        // -----------------------------------------------------------------------------------------------------------
        // The text to compile.

        /**
         * The ways to put the probe at [line] (1-based) and [column] (0-based, in UTF-16 units) of [text], to try in order until
         * one parses: the word there replaced (with `:` before it for an atom), with the rest of the line; with the brackets
         * the line opened closed instead, and then followed by ` = 0` (`val x: T` and `fn f(x: T)` need one); and the expression
         * before a `.` alone, in a `val` (a line still being written may not be a statement yet). Null when there is no such line.
         */
        fun prepare(text: String, line: Int, column: Int): Pair<Completion, List<String>>? {
            val lines = text.split("\n")
            val current = lines.getOrNull(line - 1)?.removeSuffix("\r") ?: return null
            val c = column.coerceIn(0, current.length)
            fun ident(ch: Char) = ch == '_' || ch.isLetterOrDigit()
            var ws = c
            while (ws > 0 && ident(current[ws - 1])) ws--
            var we = c
            while (we < current.length && ident(current[we])) we++
            val before = current.getOrNull(ws - 1)
            // `:name`, not `x: Type` or `f(): Type`.
            val atom = before == ':' && (ws < 2 || current[ws - 2].let { !ident(it) && it !in ":)]>?" })
            val pre = current.substring(0, if (atom) ws - 1 else ws)
            val indent = current.takeWhile { it == ' ' || it == '\t' }
            val chain = if (before == '.' && current.getOrNull(ws - 2) != '.') chainBefore(pre) else ""
            val variants = listOf(pre + PROBE + current.substring(we), pre + PROBE + closers(pre), pre + PROBE + closers(pre) + " = 0", "$indent${"val"} $HOLDER = $chain$PROBE").distinct()
            return Completion(atom) to variants.map { (lines.take(line - 1) + it + lines.drop(line)).joinToString("\n") }
        }

        /** The postfix expression [pre] ends with, up to and with its last `.` or `?.`: `a.b(c)[0].` of `val x = a.b(c)[0].`. */
        private fun chainBefore(pre: String): String {
            var i = pre.length
            while (i > 0) {
                val ch = pre[i - 1]
                when {
                    ch == '_' || ch.isLetterOrDigit() || ch == '.' || ch == '!' || ch == '?' && pre.getOrNull(i) == '.' -> i--
                    ch == ')' || ch == ']' -> {
                        val open = if (ch == ')') '(' else '['
                        var depth = 0
                        while (i > 0) {
                            val k = pre[i - 1]
                            if (k == ch) depth++ else if (k == open) depth--
                            i--
                            if (depth == 0) break
                        }
                    }
                    else -> break
                }
            }
            return pre.substring(i)
        }

        /** What closes the brackets [pre] leaves open, outside strings. */
        private fun closers(pre: String): String {
            val open = ArrayList<Char>()
            var inString = false
            var i = 0
            while (i < pre.length) {
                val ch = pre[i]
                when {
                    inString && ch == '\\' -> i++
                    ch == '"' -> inString = !inString
                    inString -> {}
                    ch == '(' || ch == '[' || ch == '{' -> open += ch
                    (ch == ')' || ch == ']' || ch == '}') && open.isNotEmpty() -> open.removeAt(open.size - 1)
                }
                i++
            }
            return (if (inString) "\"" else "") + open.reversed().joinToString("") { when (it) { '(' -> ")"; '[' -> "]"; else -> "}" } }
        }

        // -----------------------------------------------------------------------------------------------------------
        // The request.

        /**
         * The completions at [line] and [column] of [file], whose text is in [sources] (or on disk) along with the other unsaved
         * files, as JSON: `{"found":true,"context":"member","expected":"Int","receiver":"Point","items":[{"label":"x",
         * "kind":"field","detail":"Int","rank":0}, ...]}`, or `{"found":false}`.
         */
        fun answer(file: File, sources: Map<String, String>, line: Int, column: Int): String {
            val name = Util.sourceName(file)
            val text = sources[name] ?: file.takeIf { it.isFile }?.readText() ?: return NOT_FOUND
            val (completion, variants) = prepare(text, line, column) ?: return NOT_FOUND
            for (variant in variants) {
                try { Compiler.parse(variant.byteInputStream(), file.path) } catch (_: EcError) { continue }
                try {
                    Compiler.compile(file) { it.checkOnly = true; it.sources = sources + (name to variant); it.completion = completion }
                } catch (hit: Hit) {
                    return json(hit.found)
                }
            }
            return NOT_FOUND
        }

        private const val NOT_FOUND = "{\"found\":false}"

        private fun json(found: Found): String = buildString {
            append("{\"found\":true,\"context\":").append(Checker.json(found.context))
            found.expected?.let { append(",\"expected\":").append(Checker.json(it.toString())) }
            found.receiver?.let { append(",\"receiver\":").append(Checker.json(it.toString())) }
            append(",\"items\":[")
            found.items.forEachIndexed { i, item ->
                if (i > 0) append(',')
                append("{\"label\":").append(Checker.json(item.label)).append(",\"kind\":").append(Checker.json(item.kind))
                    .append(",\"detail\":").append(Checker.json(item.detail)).append(",\"rank\":").append(item.rank)
                item.module?.let { append(",\"module\":").append(Checker.json(it)) }
                if (item.params >= 0) append(",\"params\":").append(item.params)
                append('}')
            }
            append("]}")
        }
    }
}
