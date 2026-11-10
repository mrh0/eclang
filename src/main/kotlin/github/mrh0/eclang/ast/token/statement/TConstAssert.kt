package github.mrh0.eclang.ast.token.statement

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.NonValueTok
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.ast.token.function.call.TExprCall
import github.mrh0.eclang.ast.token.op.TIs
import github.mrh0.eclang.ast.token.op.compare.TEquals
import github.mrh0.eclang.ast.token.op.compare.TGreaterOrEquals
import github.mrh0.eclang.ast.token.op.compare.TGreaterThan
import github.mrh0.eclang.ast.token.op.compare.TLessOrEquals
import github.mrh0.eclang.ast.token.op.compare.TLessThan
import github.mrh0.eclang.ast.token.op.compare.TNotEquals
import github.mrh0.eclang.ast.token.op.logical.TNot
import github.mrh0.eclang.ast.token.op.requireBool
import github.mrh0.eclang.ast.token.type.TTypeByName
import github.mrh0.eclang.ast.unnested
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.Escapes
import github.mrh0.eclang.context.Lifetimes
import github.mrh0.eclang.context.Narrowing
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.ConstFold
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRConvert
import github.mrh0.eclang.ir.IRPass
import github.mrh0.eclang.ir.IRStaticAssert
import github.mrh0.eclang.util.StringLiteral
import github.mrh0.ect2.Pointer
import github.mrh0.ect2.Record
import github.mrh0.ect2.Type
import github.mrh0.ect2.TypeContext
import github.mrh0.ect2.Types

/**
 * `const assert`: checked when compiling, so it costs nothing when the program runs. It can say
 * - a condition on constants (`const assert MAX_SLOTS <= 255`); one with `sizeof`, `alignof` or `offsetof`, which only C knows,
 *   becomes a C `_Static_assert`, which the C compiler checks;
 * - a relation of two types: `A is B` (an A already is a B: the same type, a child record, a member of the union B, a record
 *   with interface B's fields and functions) or `A fits B` (an A converts to a B where one is expected, without `as`);
 * - what the compiler knows of memory (see Lifetimes): `a outlives b`, `x in p` (x's memory is in the pool p, or `pool`), and
 *   `not escapes(p)` (the function keeps nothing of its parameter p, which is known once every body is compiled, see Escapes).
 * In a generic function it is checked for each `<T>` the function is compiled for, so it can say what `<T>` must be.
 */
class TConstAssert(location: Loc, private val claim: Claim, private val message: String?, private val source: String) : NonValueTok(location) {
    sealed interface Claim
    class Condition(val condition: ITok) : Claim
    class Relation(val left: ITok, val relation: String, val right: ITok, val leftText: String, val rightText: String) : Claim
    class Outlives(val left: ITok, val right: ITok, val leftText: String, val rightText: String) : Claim
    class InPool(val value: ITok, val pool: ITok, val valueText: String) : Claim

    private var instance = ""

    private fun fail(why: String): Nothing =
        throw EcError(location, "${message?.let { "$it: " } ?: ""}`const assert $source` failed$instance: $why")

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        // In a copy of a generic function: which one, and for which call.
        val f = cd.function
        instance = if (f?.calledAt != null && f.bindings.isNotEmpty())
            " for ${f.bindings.entries.joinToString(", ") { "<${it.key}> = ${it.value}" }} (the call at ${f.calledAt.getFilename()}:${f.calledAt.getLine()})" else ""
        when (claim) {
            is Relation -> relation(cd, claim)
            is Outlives -> Lifetimes.outlivesProblem(cd, claim.left, claim.right, claim.leftText, claim.rightText)?.let { fail(it) }
            is InPool -> Lifetimes.inPoolProblem(cd, claim.value, claim.pool, claim.valueText)?.let { fail(it) }
            is Condition -> return condition(cd, claim.condition)
        }
        return Types.None to IRPass(location, null)
    }

    private fun relation(cd: CompileData, r: Relation) {
        // `const assert x is Int` of a variable x: what its type says.
        val left = r.left
        if (r.relation == "is" && left is TTypeByName && '.' !in left.type && cd.findVar(left.type) != null) {
            condition(cd, TIs(location, TNamed(left.location, left.type), r.right, negated = false))
            return
        }
        val a = left.process(cd, Types.None).first
        val b = r.right.process(cd, Types.None).first
        val cost = cd.types.cost(a, b)
        val method = cd.functions.missingMethod(a, b)
        val ok = method == null && if (r.relation == "is") cost == 0 || extends(a, b) else cost != TypeContext.NO
        if (ok) return
        val why = when {
            method != null -> "$a has no function `${method.name}` that interface $b asks for."
            cost != TypeContext.NO -> "a value of type $a converts to $b, but isn't one. Write `fits` to allow a conversion."
            else -> cd.types.explain(a, b)
        }
        fail(why)
    }

    private fun condition(cd: CompileData, condition: ITok): Pair<Type, IIR> {
        val c = condition.unnested()
        // `not escapes(p)`: the function's body keeps nothing of p.
        escapes(c, negated = false)?.let { (param, negated) ->
            if (!negated) throw EcError(location, "`const assert` can say that a parameter doesn't escape (`not escapes(p)`), not that it does.")
            val f = cd.function ?: throw EcError(location, "`not escapes(p)` is about a parameter p of the function it is in.")
            if (f.params.none { it.name == param } && f.absent.none { it.name == param }) throw EcError(location, "'$param' isn't a parameter of ${f.name}: `not escapes(p)` is about one of its parameters.")
            cd.escapes.claim(Escapes.Claim(location, f.body!!, param, message))
            return Types.None to IRPass(location, null)
        }
        Narrowing.decided(cd, condition)?.let { if (it) return Types.None to IRPass(location, null) else fail("the types say it is false.") }
        val ir = requireBool(cd, location, "The `const assert` condition", condition.process(cd, Types.None))
        return when (val value = ConstFold.eval(ir)) {
            true -> Types.None to IRPass(location, null)
            false -> fail(values(cd, c) ?: "it is false.")
            ConstFold.InC -> {
                cd.staticAsserts++
                Types.None to IRStaticAssert(location, ir, StringLiteral.of("${IRConvert.where(location).trim('"')}: ${message?.let { "$it: " } ?: ""}const assert $source"))
            }
            else -> throw EcError(location, "`const assert` needs a condition known when compiling: constants, literals, `sizeof`, `alignof`, `offsetof`, " +
                "or a relation of types (`A is B`, `A fits B`). To check a value when the program runs, use `assert`.")
        }
    }

    /** Whether the record [a] extends [b] (a child is one of its parents), also through pointers: `@Cat` is an `@Animal`. */
    private fun extends(a: Type, b: Type): Boolean = when {
        a is Record && b is Record -> a.distanceTo(b) != null
        a is Pointer && b is Pointer -> extends(a.target, b.target)
        else -> false
    }

    /** `escapes(p)` or `not escapes(p)`: p, and whether it is negated. */
    private fun escapes(t: ITok, negated: Boolean): Pair<String, Boolean>? = when (t) {
        is TNot -> escapes(t.expr.unnested(), !negated)
        is TExprCall -> if (t.name == "escapes" && !t.isMethod && t.args.size == 1) (t.args[0].unnested() as? TNamed)?.let { it.name to negated }
            ?: throw EcError(location, "`escapes(p)` takes the name of a parameter.") else null
        else -> null
    }

    /** "it is false: MAX = 300", from the sides of a comparison that are known. */
    private fun values(cd: CompileData, c: ITok): String? {
        val sides = when (c) {
            is TEquals -> listOf(c.left, c.right)
            is TNotEquals -> listOf(c.left, c.right)
            is TLessThan -> listOf(c.left, c.right)
            is TLessOrEquals -> listOf(c.left, c.right)
            is TGreaterThan -> listOf(c.left, c.right)
            is TGreaterOrEquals -> listOf(c.left, c.right)
            else -> return null
        }
        val shown = sides.filter { it.unnested() is TNamed }.mapNotNull { side ->
            val value = try { ConstFold.eval(side.process(cd, Types.None).second) } catch (_: EcError) { null } ?: return@mapNotNull null
            "${(side.unnested() as TNamed).name} = $value"
        }
        return if (shown.isEmpty()) null else "it is false, as ${shown.joinToString(" and ")}."
    }

    override fun toString() = "TConstAssert($source)"
}
