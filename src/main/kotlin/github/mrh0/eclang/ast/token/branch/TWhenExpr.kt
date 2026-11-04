package github.mrh0.eclang.ast.token.branch

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.TBlock
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.ir.IRRaw
import github.mrh0.eclang.ir.IRStatement
import github.mrh0.eclang.ir.branch.IRWhenAssign
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * `when x do` as a value: `val y = when x do ...`. The arms are a [TStatementWhen] before the statement it is in, each
 * setting a temporary, whose type fits every arm's value. An arm may also leave (`ret`, `throw`, a call that returns
 * Never) instead of giving a value. Every case must be handled, so without a complete set of arms it needs `else do`.
 */
class TWhenExpr(location: Loc, private val binding: String?, private val mutable: Boolean, val subject: ITok, private val arms: List<WhenArm>) : Tok(location) {
    /** The values the `when` may give, and the subject when an arm can name it; see Lifetimes. */
    val flows: List<ITok> get() = arms.filter { it.isValue }.map { it.body } + if (binding != null || arms.any { it.binding != null }) listOf(subject) else listOf()

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        if (cd.hoistProblem() != null) throw cd.needsStatement(location, "A `when` used as a value", "Assign it to a variable in a statement of its own first.")
        var type: Type = Types.Never
        val temp = cd.declareTemp(location) { type }
        val values = ArrayList<Value>()
        val bodies = arms.map { arm ->
            arm.with(if (arm.isValue) TBlock(arm.location, listOf(Value(arm.location, arm.body, hint, temp, values))) else Leaving(arm.location, arm.body))
        }
        val (_, chain) = TStatementWhen(location, binding, mutable, subject, bodies, exhaustive = true).process(cd, Types.None)
        type = resultType(cd, hint, values.map { it.compiled.first })
        values.forEach { it.assign.value = cd.expect(it.location, it.compiled, type, "this arm of `when`") }
        cd.hoist(location, chain)
        return type to IRRaw(location, if (type === Types.Never) "(void)0" else temp)
    }

    /** The type of the `when`: the expected type if every value fits, a type that fits them all, or their union. */
    private fun resultType(cd: CompileData, hint: Type, values: List<Type>): Type {
        val types = cd.types
        if (values.isEmpty()) return Types.Never
        if (hint !== Types.None && values.all { types.accepts(hint, it) }) return hint
        val fits = values.firstOrNull { t -> values.all { types.accepts(t, it) } }
        return cd.storable(location, fits ?: types.union(values.map { types.concrete(it) }), "the value of `when`")
    }

    override fun toString() = "TWhenExpr($subject, ${arms.size} arms)"

    /** The value of an arm, stored in the `when`'s [temp]. */
    private class Value(location: Loc, private val value: ITok, private val hint: Type, temp: String, private val values: MutableList<Value>) : Tok(location) {
        val assign = IRWhenAssign(location, temp)
        lateinit var compiled: Pair<Type, IIR>

        override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
            val v = value.process(cd, this.hint)
            if (v.first === Types.Never) {
                cd.ctx().setReturned()
                return Types.None to IRStatement(location, v.second)
            }
            compiled = cd.usable(location, v, "an arm of `when`")
            values += this
            return Types.None to assign
        }
    }

    /** An arm written as statements: it has no value to give, so it must leave. */
    private class Leaving(location: Loc, private val body: ITok) : Tok(location) {
        override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
            val ir = body.process(cd, hint)
            if ((ir.second as IRBlock).diverges || cd.functionHasErrors) return ir
            throw EcError(location, "This arm of a `when` used as a value has no value. Write the value after `do` (`1 do \"one\"`), or make the arm leave with `ret`, `throw`, `break` or `continue`.")
        }
    }
}
