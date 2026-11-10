package github.mrh0.eclang.ast.token.statement

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.ast.token.accessor.TAccessor
import github.mrh0.eclang.ast.token.accessor.TAccessorNamed
import github.mrh0.eclang.ast.token.data.TAtom
import github.mrh0.eclang.ast.token.data.TBoolean
import github.mrh0.eclang.ast.token.data.TCString
import github.mrh0.eclang.ast.token.data.TChar
import github.mrh0.eclang.ast.token.data.TFloat
import github.mrh0.eclang.ast.token.data.TInteger
import github.mrh0.eclang.ast.token.data.TInterpolated
import github.mrh0.eclang.ast.token.data.TNull
import github.mrh0.eclang.ast.token.data.TString
import github.mrh0.eclang.ast.token.function.call.Builtins
import github.mrh0.eclang.ast.token.function.call.TExprCall
import github.mrh0.eclang.ast.token.op.TCastNotNull
import github.mrh0.eclang.ast.token.op.TIs
import github.mrh0.eclang.ast.token.op.TReadPointer
import github.mrh0.eclang.ast.token.op.compare.TEquals
import github.mrh0.eclang.ast.token.op.compare.TEqualsEquals
import github.mrh0.eclang.ast.token.op.compare.TGreaterOrEquals
import github.mrh0.eclang.ast.token.op.compare.TGreaterThan
import github.mrh0.eclang.ast.token.op.compare.TLessOrEquals
import github.mrh0.eclang.ast.token.op.compare.TLessThan
import github.mrh0.eclang.ast.token.op.compare.TNotEquals
import github.mrh0.eclang.ast.token.op.compare.TNotEqualsEquals
import github.mrh0.eclang.ast.token.op.logical.TAnd
import github.mrh0.eclang.ast.token.op.logical.TNot
import github.mrh0.eclang.ast.token.op.requireBool
import github.mrh0.eclang.ast.unnested
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.Narrowing
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.ConstFold
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRAssert
import github.mrh0.eclang.ir.IRHeldType
import github.mrh0.eclang.ir.IRRaw
import github.mrh0.eclang.ir.IRTempDefine
import github.mrh0.eclang.util.StringLiteral
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types
import github.mrh0.ect2.Union

/** A value compiled already, as a token: the operand of an assertion, held in a temporary (see [Assertions]). */
class TValue(location: Loc, private val value: Pair<Type, IIR>, private val original: ITok) : Tok(location) {
    override fun pointerFlow(): PointerFlow = PointerFlow.From(original)
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> = value
    override fun toString() = "TValue($original)"
}

/**
 * The runtime checks of `assert`, `requires` and `ensures`. A failed check reports what failed and the values it compared:
 *
 *     main.ec:12: assertion failed in merge: a.len == b.len
 *         a.len = 3
 *         b.len = 4
 *
 * `a and b` is checked as `a`, then `b` (which only runs when `a` held, and sees what `a` proves), and the report says which
 * part failed. The two sides of a comparison and the value an `is` checks are shown; when one of them runs code (a call),
 * they are kept in temporaries, so each runs once, in order. Arguments of a call that is the condition are shown when they
 * are plain variables or fields. The report is only built when the check fails, in the current pool.
 */
object Assertions {
    /**
     * Compiles the check of [condition], written [source] (with [sources], the text of its parts). [keyword] is `assert`,
     * `requires` or `ensures`, [header] starts the report ("assertion failed in f"), [message] is what to say instead, a String,
     * and [at] the C expression of the place to report. What the condition proves holds in the current scope afterwards.
     */
    fun compile(cd: CompileData, location: Loc, condition: ITok, source: String, sources: Map<ITok, String>, message: ITok?, keyword: String, header: String, at: String): List<IIR> {
        val parts = conjuncts(condition)
        val irs = mutableListOf<IIR>()
        parts.forEachIndexed { i, part ->
            val partSource = if (parts.size > 1) sources[part] else null
            val compile = { check(cd, location, part, source, partSource, sources, message, keyword, header, at) }
            irs += if (i == 0) compile() else cd.noHoist("`try` can't be used after `and` in `$keyword`, because that part only runs when the parts before it held. Make it a check of its own: `$keyword ...` on the next line.") { compile() }
            val proved = Narrowing.of(cd, part, true)
            Narrowing.apply(cd, proved)
            if (cd.ctx().stack.debugDepth > 0) proved.keys.forEach { cd.ctx().stack.debugNarrowed.putIfAbsent(it, location) }
        }
        return irs
    }

    /** `assert false`: the report, without a condition. */
    fun fail(cd: CompileData, location: Loc, message: ITok?, header: String, at: String): IIR {
        val (text, report) = cd.noHoist("`try` can't be used in the message of `assert`, which is only made when the check fails.") {
            report(cd, location, "false", null, message, header, listOf())
        }
        return IRAssert(location, null, at, text, report)
    }

    /** `a and b and c` as `a`, `b`, `c`. */
    private fun conjuncts(t: ITok): List<ITok> = when (val u = t.unnested()) {
        is TAnd -> conjuncts(u.left) + conjuncts(u.right)
        else -> listOf(t)
    }

    private fun check(cd: CompileData, location: Loc, part: ITok, source: String, partSource: String?, sources: Map<ITok, String>, message: ITok?, keyword: String, header: String, at: String): List<IIR> {
        val temps = mutableListOf<IIR>()
        // What the report shows: the text of a value and the value, compiled where the check fails.
        val shown = mutableListOf<Pair<String, ITok>>()
        val leaf = part.unnested()
        val (left, right) = operands(leaf)
        val checked: ITok = if (left != null) {
            val values = listOfNotNull(left, right)
            val nullCheck = values.any { it.unnested() is TNull }
            // When a side runs code, each side is kept in a temporary, in order, so that it runs once.
            val capture = !nullCheck && values.any { !isPure(it) }
            val replaced = values.map { v ->
                if (nullCheck || isLiteral(v)) return@map v
                if (!capture) {
                    sources[v]?.let { shown += it to v }
                    return@map v
                }
                val value = cd.usable(location, v.process(cd, Types.None), "`$keyword`")
                // A constant has nothing to show, and a value C can't hold (a literal) stays where it is.
                if (value.second.isConstant() || value.first !== cd.types.concrete(value.first) || cd.types.storageProblem(value.first) != null)
                    return@map TValue(location, value, v)
                val temp = cd.temp()
                temps += IRTempDefine(location, temp, value.first, value.second)
                val read = TValue(location, value.first to IRRaw(location, temp), v)
                sources[v]?.let { shown += it to read }
                read
            }
            rebuild(leaf, replaced[0], replaced.getOrNull(1))
        } else {
            // `f(x, y)` or `not f(x, y)`: the arguments that are plain variables or fields.
            val call = (leaf as? TNot)?.expr?.unnested() ?: leaf
            if (call is TExprCall) call.args.filter { isPure(it) && !isLiteral(it) }.forEach { a -> sources[a]?.let { shown += it to a } }
            part
        }
        val what = if (keyword == "assert") "The `assert` condition" else "The condition of `$keyword`"
        val condition = requireBool(cd, location, what, checked.process(cd, Types.None))
        decided(cd, location, part, condition, keyword)

        val (text, report) = cd.noHoist("`try` can't be used in the message of `$keyword`, which is only made when the check fails. Handle the error with `??`, or compute the message before.") {
            report(cd, location, source, partSource, message, header, shown)
        }
        return temps + IRAssert(location, condition, at, text, report)
    }

    /** The values a comparison or `is` compares, or nulls for another condition. */
    private fun operands(t: ITok): Pair<ITok?, ITok?> = when (t) {
        is TEquals -> t.left to t.right
        is TNotEquals -> t.left to t.right
        is TEqualsEquals -> t.left to t.right
        is TNotEqualsEquals -> t.left to t.right
        is TLessThan -> t.left to t.right
        is TLessOrEquals -> t.left to t.right
        is TGreaterThan -> t.left to t.right
        is TGreaterOrEquals -> t.left to t.right
        is TIs -> t.expr to null
        else -> null to null
    }

    /** The comparison or `is` [t] with the operands [left] and [right] instead. */
    private fun rebuild(t: ITok, left: ITok, right: ITok?): ITok {
        val l = (t as Tok).location
        return when (t) {
            is TEquals -> TEquals(l, left, right!!)
            is TNotEquals -> TNotEquals(l, left, right!!)
            is TEqualsEquals -> TEqualsEquals(l, left, right!!)
            is TNotEqualsEquals -> TNotEqualsEquals(l, left, right!!)
            is TLessThan -> TLessThan(l, left, right!!)
            is TLessOrEquals -> TLessOrEquals(l, left, right!!)
            is TGreaterThan -> TGreaterThan(l, left, right!!)
            is TGreaterOrEquals -> TGreaterOrEquals(l, left, right!!)
            is TIs -> TIs(l, left, t.type, t.negated)
            else -> t
        }
    }

    /** Whether [t] reads a value without running code, so reading it again where the check fails gives the same value. */
    private fun isPure(t: ITok): Boolean = when (val u = t.unnested()) {
        is TNamed, is TValue -> true
        is TReadPointer -> isPure(u.expr)
        is TAccessorNamed -> isPure(u.expr)
        is TAccessor -> isPure(u.expr) && isPure(u.index)
        is TCastNotNull -> isPure(u.expr)
        else -> isLiteral(u)
    }

    /** Whether [t] is a literal, which the report doesn't show. */
    private fun isLiteral(t: ITok): Boolean = when (t.unnested()) {
        is TInteger, is TFloat, is TString, is TCString, is TChar, is TBoolean, is TNull, is TAtom, is TInterpolated -> true
        else -> false
    }

    /**
     * An error for a check that always fails, and a warning for one that can't fail, known from the types (`x is Int` of an Int) or
     * from constants. Not in a copy of a function made for one member of a union or for a `<T>`, where that is expected.
     */
    private fun decided(cd: CompileData, location: Loc, part: ITok, condition: IIR, keyword: String) {
        val f = cd.function
        if (f != null && (f.isUnionCopy || f.bindings.isNotEmpty())) return
        val value = Narrowing.decided(cd, part) ?: ConstFold.bool(condition) ?: return
        if (value) cd.warn(location, "This `$keyword` always holds, so it checks nothing: the types or constants already decide it. " +
            "Remove it, or make it `const assert` to have the compiler check it.")
        else throw EcError(location, "This `$keyword` always fails: the types or constants already decide it. If this code must never run, write `panic(\"why\")`.")
    }

    /** The text of the report when it shows nothing, or the String with what it shows, as the second. */
    private fun report(cd: CompileData, location: Loc, source: String, partSource: String?, message: ITok?, header: String, shown: List<Pair<String, ITok>>): Pair<StringLiteral, IIR?> {
        val parts = mutableListOf<Any>()
        val plain = StringBuilder()
        fun text(s: String) { parts += StringLiteral.of(s); plain.append(s) }
        if (message != null) {
            val value = message.process(cd, Types.String)
            val t = cd.types.concrete(value.first)
            if (t !== Types.String && t !== Types.CString) throw EcError(location, "The message after the condition must be a String, but it is ${value.first}. Put the value in one: `\"... \${x}\"`.")
            parts += TInterpolated.Field(TValue(location, value, message), null)
            text("\n    $header: $source")
        } else text("$header: $source")
        if (partSource != null && partSource != source) text("\n    the part that failed: $partSource")
        for ((name, tok) in shown) {
            val value = try { cd.usable(location, tok.process(cd, Types.None), "the report") } catch (_: EcError) { continue }
            // A union that can't be printed: what it holds.
            val union = cd.types.concrete(value.first) as? Union
            if (!Builtins.canShow(cd, value.first) && union != null) {
                text("\n    $name holds ")
                parts += TInterpolated.Field(TValue(location, Types.CString to IRHeldType(location, value.second, union), tok), null)
                continue
            }
            if (!Builtins.canShow(cd, value.first)) continue
            val quoted = cd.types.concrete(value.first).let { it === Types.String || it === Types.CString }
            text("\n    $name = ${if (quoted) "\"" else ""}")
            parts += TInterpolated.Field(TValue(location, value, tok), null)
            if (quoted) text("\"")
        }
        val literal = StringLiteral.of(plain.toString())
        if (parts.none { it is TInterpolated.Field }) return literal to null
        return literal to Builtins.interpolate(cd, location, Builtins.current(location), TInterpolated(location, parts)).second
    }
}
