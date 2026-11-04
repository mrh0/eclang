package github.mrh0.eclang.ast.token.statement

import github.mrh0.eclang.ast.IAssignable
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Place
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.unnested
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.ast.token.op.Ops
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.Lifetimes
import github.mrh0.eclang.ast.token.accessor.TAccessor
import github.mrh0.eclang.ast.token.accessor.TAccessorNamed
import github.mrh0.eclang.ast.token.accessor.alignedAnywhere
import github.mrh0.eclang.ast.token.accessor.inPacked
import github.mrh0.eclang.ast.token.type.checkBits
import github.mrh0.eclang.ast.token.op.TReadPointer
import github.mrh0.eclang.context.state.NarrowedVar
import github.mrh0.eclang.error.EcAssignConstError
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRNamed
import github.mrh0.eclang.ir.IRStatementAssign
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.eclang.ast.token.op.RuntimeChecks
import github.mrh0.eclang.ir.arithmetic.IRCheckedCompound
import github.mrh0.ect2.IntLiteral
import github.mrh0.ect2.Prim
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * `target = value`, or `target op= value` with [op] one of `+ - * / % & | ^ << >>` (empty for a plain `=`). The
 * target is a variable, a field (`p.x`), an element (`a[i]`) or what a pointer points to (`@p`).
 */
class TStatementAssign(location: Loc, private val target: ITok, private val op: String, private val expr: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val place = when (val t = target.unnested()) {
            is TNamed -> variable(cd, t.name, narrowed = op.isNotEmpty())
            is IAssignable -> t.place(cd).also { p -> p.readOnly?.let { throw EcError(location, it) } }
            else -> throw EcError(location, "Can't assign to this expression. You can assign to a variable (`x = 1`), a field (`p.x = 1`), an element (`a[i] = 1`) or what a pointer points to (`@p = 1`).")
        }
        var valueType: Type = place.type
        val value = if (op.isEmpty()) expr.process(cd, place.type).let { v ->
            (target.unnested() as? TAccessorNamed)?.let { t -> t.bitFieldOf?.let { record -> checkBits(location, record, t.fixedName, place.type, v.first) } }
            cd.expect(location, v, place.type, "assignment to ${describe()}")
        } else compound(cd, place.type) { valueType = it }
        if (op.isEmpty()) Lifetimes.assign(cd, location, target, expr, place.type, describe())
        place.name?.let { cd.ctx().forget(it) }
        if (op.isNotEmpty() && RuntimeChecks.needed(cd, op, place.type, valueType)) {
            val count = if (op == "<<" || op == ">>") Ops.base(cd.types.concrete(valueType)) as Prim else null
            return Types.None to IRCheckedCompound(location, place.ir, op, Ops.base(place.type) as Prim, count, value, cd.temp(), noPointer(place.type))
        }
        return Types.None to IRStatementAssign(location, place.ir, op, value)
    }

    /**
     * Whether a checked `target op= value` can't go through a pointer to the target, a bit-field or a field of a `packed`
     * record. It then reads the target twice, so the target must not have side effects.
     */
    private fun noPointer(type: Type): Boolean {
        val t = target.unnested()
        if (!((t as? TAccessorNamed)?.bitFieldOf != null || (inPacked(t) && !alignedAnywhere(type)))) return false
        if (!repeatable(t)) throw EcError(location, "`$op=` would run the calls in this target twice, because of how this field is stored. Write it out: `x.f = x.f $op value`, with the calls' result in a variable.")
        return true
    }

    /** Whether evaluating [tok] twice is the same as once: only variables and fields. */
    private fun repeatable(tok: ITok): Boolean = when (val t = tok.unnested()) {
        is TNamed -> true
        is TAccessorNamed -> repeatable(t.expr)
        is TReadPointer -> repeatable(t.expr)
        else -> false
    }

    /**
     * The variable [name] as a whole. A plain assignment ends any narrowing, so it takes the declared type; with
     * [narrowed] (`x += 1` inside `if x != null`) the value keeps its narrowed type, which stays true.
     */
    private fun variable(cd: CompileData, name: String, narrowed: Boolean): Place {
        if (TNamed.isCurrentPool(cd, name)) throw EcError(location, TNamed(location, name).place(cd).readOnly!!)
        val fixedName = testIdentifier(location, name)
        val found = cd.getVar(location, fixedName)
        val ivar = (found as? NarrowedVar)?.base ?: found
        if (!ivar.canAssign()) throw EcAssignConstError(location, name, ivar.kind())
        if (narrowed && found is NarrowedVar) return Place(found.getType(), found.toIR(location, cd, Types.None))
        return Place(ivar.getType(), IRNamed(location, ivar.cName()), null, fixedName)
    }

    /** The value of `target op= value`, converted to the target's type [t]. */
    private fun compound(cd: CompileData, t: Type, valueType: (Type) -> Unit): IIR {
        val what = "`$op=`"
        val v = cd.usable(location, expr.process(cd, t), "right side of $what")
        valueType(v.first)
        if (op == "+" && t === Types.String)
            throw EcError(location, "Joining Strings needs memory, so `+=` can't do it. Write `s = concat(s, other)` (or `s = \"\${s}\${other}\"`), which allocates in the current pool.")
        val integerOnly = op in setOf("%", "&", "|", "^", "<<", ">>")
        if (!Ops.isNumeric(t) || !Ops.isNumeric(v.first) || (integerOnly && (!Ops.isInteger(t) || !Ops.isInteger(v.first)))) {
            val hint = if (t === Types.Bool && op in setOf("&", "|", "^")) " For Bools write `x = x ${mapOf("&" to "and", "|" to "or", "^" to "xor")[op]} y`." else ""
            throw EcError(location, "$what needs ${if (integerOnly) "whole numbers" else "numbers"}, but ${describe()} is a $t and the value is a ${v.first}.$hint")
        }
        val literal = (v.first as? IntLiteral)?.value
        if ((op == "/" || op == "%") && literal == 0L) throw EcError(location, "$what by zero: a division by zero has no result.")
        if (op == "<<" || op == ">>") {
            if (literal != null && literal < 0) throw EcError(location, "Can't shift by a negative number of bits ($literal).")
            val bits = (Ops.base(t) as Prim).bits
            if (literal != null && literal >= bits) throw EcError(location, "Can't shift a $t by $literal bits: it only has $bits.")
            return cd.expect(location, v, cd.types.concrete(v.first), "right side of $what")
        }
        return cd.expect(location, v, t, "right side of $what on ${describe()}")
    }

    private fun describe(): String = when (val t = target.unnested()) {
        is TNamed -> "'${t.name}'"
        is TAccessorNamed -> "field '${t.name}'"
        is TAccessor -> "this element"
        is TReadPointer -> "what the pointer points to"
        else -> "this target"
    }

    override fun toString(): String {
        return "TStatementAssign($target $op= $expr)"
    }
}
