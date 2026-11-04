package github.mrh0.eclang.ast.token.branch

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.TBlock
import github.mrh0.eclang.ast.token.TExprNest
import github.mrh0.eclang.ast.token.TFixed
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.ast.token.data.TAtom
import github.mrh0.eclang.ast.token.data.TBoolean
import github.mrh0.eclang.ast.token.data.TNull
import github.mrh0.eclang.ast.token.op.TIs
import github.mrh0.eclang.ast.token.op.TRange
import github.mrh0.eclang.ast.token.op.compare.TEquals
import github.mrh0.eclang.ast.token.op.compare.TGreaterOrEquals
import github.mrh0.eclang.ast.token.op.compare.TLessOrEquals
import github.mrh0.eclang.ast.token.op.compare.TLessThan
import github.mrh0.eclang.ast.token.op.logical.TAnd
import github.mrh0.eclang.ast.token.op.logical.TOr
import github.mrh0.eclang.ast.token.statement.TStatementDefineVal
import github.mrh0.eclang.ast.token.statement.TStatementDefineVar
import github.mrh0.eclang.ast.unnested
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.state.InlineConstant
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.ir.IRRaw
import github.mrh0.eclang.ir.IRTempDefine
import github.mrh0.eclang.util.Util
import github.mrh0.ect2.*

/**
 * One arm of a `when`: `is T do` ([type]), `a, b do` ([values], which may be ranges), `n if c do` ([binding]: n names the
 * value) or `else do` (none of these). [guard]: the arm only matches when it holds too. [isValue]: in a `when` used as a
 * value, [body] is the arm's value rather than statements.
 */
class WhenArm(val location: Loc, val type: ITok?, val values: List<ITok>, val body: ITok, val guard: ITok? = null, val binding: String? = null, val isValue: Boolean = false) {
    val isElse get() = type == null && values.isEmpty() && binding == null

    /** This arm with the statements [body]. */
    fun with(body: ITok) = WhenArm(location, type, values, body, guard, binding)
}

/**
 * `when x do` with arms `is T do`, `v1, v2 do` and `else do`: runs the first arm that matches. It is an `if`/`else if`
 * chain on `x is T` and `x == v`, so a variable x is narrowed in each arm as it would be there. `when val n = expr do`
 * names the value; another expression is evaluated once, into a temporary.
 *
 * Without `else`, a `when` on a union (or a Bool) must handle every member: `is T`, `null` and atom arms cover them
 * (an arm with a guard covers nothing, since it may not match). [exhaustive]: every other `when` must too, with `else`.
 */
class TStatementWhen(
    location: Loc, private val binding: String?, private val mutable: Boolean, private val subject: ITok, private val arms: List<WhenArm>,
    private val exhaustive: Boolean = false,
) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val elseArms = arms.filter { it.isElse }
        if (elseArms.size > 1 || (elseArms.size == 1 && arms.last() !== elseArms[0]))
            throw EcError(elseArms.last().location, "A `when` has at most one `else do` arm, and it comes last: arms after it could never run.")
        if (binding != null) {
            val define = if (mutable) TStatementDefineVar(location, binding, subject) else TStatementDefineVal(location, binding, subject)
            val ir = TBlock(location, listOf(define, WhenOn(location, TNamed(location, binding), arms, exhaustive))).process(cd, hint).second as IRBlock
            if (ir.diverges) cd.ctx().setReturned()
            return Types.None to ir
        }
        if (subject.unnested() is TNamed) return WhenOn(location, subject, arms, exhaustive).process(cd, hint)
        // Any other value is evaluated once, into a temporary.
        val value = cd.usable(location, subject.process(cd, Types.None), "the value of `when`")
        val type = cd.storable(location, value.first, "the value of `when`")
        val temp = cd.temp()
        val define = IRTempDefine(location, temp, type, cd.expect(location, value, type, "the value of `when`"))
        val (_, chain) = WhenOn(location, TFixed(location, type, IRRaw(location, temp)), arms, exhaustive).process(cd, hint)
        return Types.None to IRBlock(location, listOf(define, chain), listOf(), cd.ctx().hasReturned())
    }

    override fun toString() = "TStatementWhen($subject, ${arms.size} arms)"
}

/** [inner] compiled with [name] standing for [subject], for the condition and the body of an arm `n if c do`. */
private class Bound(location: Loc, private val name: String, private val subject: ITok, private val inner: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        cd.ctx().push()
        try {
            val (type, ir) = subject.process(cd, Types.None)
            cd.ctx().define(location, InlineConstant(Util.testIdentifier(location, name), type, ir))
            return inner.process(cd, hint)
        } finally {
            cd.ctx().pop()
        }
    }
}

/** The arms of a `when` on [subject] (a name, or a [TFixed] value), as an `if` chain. */
private class WhenOn(location: Loc, private val subject: ITok, private val arms: List<WhenArm>, private val exhaustive: Boolean) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val type = subjectType(cd)
        var elseArm = arms.lastOrNull()?.takeIf { it.isElse }
        if (elseArm == null) missing(cd, type)?.let { throw EcError(location, it) }
        // When the arms handle every member of a union (or both Bools), the last one is all that is left: it is the
        // `else`, so a `when` whose arms all `ret` ends the function.
        if (elseArm == null && (type is Union || type === Types.Bool) && (arms.size > 1 || exhaustive) && arms.last().guard == null) elseArm = arms.last()
        if (elseArm == null && exhaustive) throw EcError(location, "A `when` used as a value must give a value in every case, but these arms may not match. Add an `else do` arm for the rest.")
        val matching = arms.filter { it !== elseArm }
        val conditions = matching.map { arm -> condition(arm) }
        val bodies = matching.map { arm -> arm.binding?.let { Bound(arm.location, it, subject, arm.body) } ?: arm.body }
        return TStatementIf(location, conditions, bodies, elseArm?.body).process(cd, hint)
    }

    /** What makes [arm] match, as a condition on the subject. */
    private fun condition(arm: WhenArm): ITok {
        if (arm.binding != null) return Bound(arm.location, arm.binding, subject, arm.guard!!)
        val match = if (arm.type != null) TIs(arm.location, subject, arm.type, false)
            else arm.values.map { matches(arm.location, it) }.reduce { a, b -> TOr(arm.location, a, b) }
        return if (arm.guard != null) TAnd(arm.location, TExprNest(arm.location, match), arm.guard) else match
    }

    /** Whether the subject equals [value], or is in it when it is a range. */
    private fun matches(at: Loc, value: ITok): ITok {
        val range = value.unnested() as? TRange ?: return TEquals(at, subject, value)
        val low = range.from?.let { TGreaterOrEquals(at, subject, it) }
        val high = range.to?.let { if (range.inclusive) TLessOrEquals(at, subject, it) else TLessThan(at, subject, it) }
        return if (low != null && high != null) TExprNest(at, TAnd(at, low, high)) else low ?: high!!
    }

    private fun subjectType(cd: CompileData): Type = when (val s = subject.unnested()) {
        // In one copy of a function with a union parameter, the parameter has one member's type, but the `when` must
        // still handle the whole union (the copies share one body).
        is TNamed -> cd.function?.declaredUnions?.get(s.name)?.takeIf { cd.ctx().getRaw(Util.testIdentifier(s.location, s.name))?.kind() == "a parameter" }
            ?: cd.getVar(location, Util.testIdentifier(s.location, s.name)).getType()
        else -> s.process(cd, Types.None).first
    }

    /** What a `when` without `else` on a [type] doesn't handle, as an error, or null when it handles everything. */
    private fun missing(cd: CompileData, type: Type): String? {
        val members: List<Any> = when {
            type is Union -> type.members
            type === Types.Bool -> listOf(true, false)
            else -> return null
        }
        val covered = HashSet<Any>()
        for (arm in arms) {
            if (arm.guard != null) continue
            arm.type?.let { tok ->
                val t = tok.process(cd, Types.None).first
                members.filterIsInstance<Type>().filter { it === t || cd.types.isSubtype(it, t) }.forEach { covered += it }
            }
            for (v in arm.values) when (val u = v.unnested()) {
                is TNull -> covered += Types.Null
                is TBoolean -> covered += u.value
                is TAtom -> covered += cd.types.atom(cd.atoms.get(u.value).label)
                else -> {}
            }
        }
        val left = members.filter { it !in covered }
        if (left.isEmpty()) return null
        val names = left.joinToString(", ") { if (it is Type && it === Types.Null) "null" else it.toString() }
        val arms = left.joinToString(", ") {
            when {
                it === Types.Null -> "`null do`"
                it is Boolean -> "`$it do`"
                it is AtomLiteral -> "`$it do`"
                else -> "`is $it do`"
            }
        }
        return "This `when` doesn't handle every value a $type can be: $names ${if (left.size == 1) "is" else "are"} missing. Add ${arms}, or an `else do` arm for the rest."
    }
}
