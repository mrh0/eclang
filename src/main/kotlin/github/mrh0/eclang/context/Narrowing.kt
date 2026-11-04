package github.mrh0.eclang.context

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.token.TExprNest
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.ast.token.data.TNull
import github.mrh0.eclang.ast.token.op.TIs
import github.mrh0.eclang.ast.token.op.compare.TEquals
import github.mrh0.eclang.ast.token.op.compare.TNotEquals
import github.mrh0.eclang.ast.token.op.logical.TAnd
import github.mrh0.eclang.ast.token.op.logical.TNot
import github.mrh0.eclang.ast.token.op.logical.TNotNot
import github.mrh0.eclang.ast.token.op.logical.TOr
import github.mrh0.eclang.util.Util
import github.mrh0.ect2.*

/**
 * Flow-sensitive typing: what a condition proves about local variables.
 *
 * Inside `if x != null do`, x can't be null, so it is narrowed from `CString?` to `CString` there. The same
 * works for `x is T`, `not`, `and`, `or`, and for the code after an `if` whose body always leaves
 * (`if x == null do ret 0` narrows x for the rest of the block). Only local variables and parameters are
 * narrowed, and assigning to a variable forgets what was known about it. A local whose address the function takes
 * (`addrof x`) is never narrowed: a write through that pointer could change it after the check.
 */
object Narrowing {
    /** Variable name → the narrower type it has when [condition] is [whenTrue]. */
    fun of(cd: CompileData, condition: ITok, whenTrue: Boolean): Map<String, Type> = when (condition) {
        is TExprNest -> of(cd, condition.expr, whenTrue)
        is TNot -> of(cd, condition.expr, !whenTrue)
        is TAnd -> if (whenTrue) of(cd, condition.left, true) + of(cd, condition.right, true) else mapOf()
        is TOr -> if (!whenTrue) of(cd, condition.left, false) + of(cd, condition.right, false) else mapOf()
        is TEquals -> nullCheck(cd, condition.left, condition.right, isNull = whenTrue)
        is TNotEquals -> nullCheck(cd, condition.left, condition.right, isNull = !whenTrue)
        is TNotNot -> nullCheck(cd, condition.expr, null, isNull = !whenTrue)
        is TIs -> isCheck(cd, condition, matches = whenTrue != condition.negated)
        else -> mapOf()
    }

    /**
     * True or false when [condition] is decided by the types alone, otherwise null. This matters for functions
     * with union parameters, which are compiled once per member type: in the `CString` copy of
     * `fn f(v: Int | CString)`, `v is Int` is always false, so that branch is skipped (not even type-checked).
     */
    fun constant(cd: CompileData, condition: ITok): Boolean? =
        if (cd.function?.isUnionCopy == true) decided(cd, condition) else null

    private fun decided(cd: CompileData, condition: ITok): Boolean? = when (condition) {
        is TExprNest -> decided(cd, condition.expr)
        is TNot -> decided(cd, condition.expr)?.not()
        is TAnd -> {
            val a = decided(cd, condition.left)
            val b = decided(cd, condition.right)
            if (a == false || b == false) false else if (a == true && b == true) true else null
        }
        is TOr -> {
            val a = decided(cd, condition.left)
            val b = decided(cd, condition.right)
            if (a == true || b == true) true else if (a == false && b == false) false else null
        }
        is TIs -> isConstant(cd, condition)?.let { it != condition.negated }
        is TEquals -> nullConstant(cd, condition.left, condition.right)
        is TNotEquals -> nullConstant(cd, condition.left, condition.right)?.not()
        else -> null
    }

    /** `x == null` decided by x's type: always true for a Null, always false for a type that can't be null. */
    private fun nullConstant(cd: CompileData, left: ITok, right: ITok): Boolean? {
        val (_, type) = when {
            right is TNull -> local(cd, left)
            left is TNull -> local(cd, right)
            else -> null
        } ?: return null
        return when {
            type === Types.Null -> true
            type is Union && type.isNullable -> null
            else -> false
        }
    }

    private fun isConstant(cd: CompileData, check: TIs): Boolean? {
        val (_, type) = local(cd, check.expr) ?: return null
        val target = check.type.process(cd, Types.None).first
        if (type is Pointer && target is Pointer) return if (cd.types.isSubtype(type, target)) true else null
        if (type !is Union) return cd.types.isSubtype(type, target)
        val fitting = type.members.filter { it === target || cd.types.isSubtype(it, target) }
        return when (fitting.size) {
            0 -> false
            type.members.size -> true
            else -> null
        }
    }

    /** Applies [narrowings] to the current scope. */
    fun apply(cd: CompileData, narrowings: Map<String, Type>) = narrowings.forEach { (name, type) -> cd.ctx().narrow(name, type) }

    private fun local(cd: CompileData, tok: ITok?): Pair<String, Type>? {
        if (tok !is TNamed) return null
        val name = Util.testIdentifier(tok.location, tok.name)
        val variable = cd.ctx().getRaw(name) ?: return null
        return name to variable.getType()
    }

    private fun nullCheck(cd: CompileData, left: ITok, right: ITok?, isNull: Boolean): Map<String, Type> {
        val (name, type) = when {
            right == null -> local(cd, left)
            right is TNull -> local(cd, left)
            left is TNull -> local(cd, right)
            else -> null
        } ?: return mapOf()
        if (type !is Union || !type.isNullable) return mapOf()
        return unlessAliased(cd, left.takeIf { it is TNamed } ?: right!!, mapOf(name to if (isNull) Types.Null else cd.types.nonNull(type)), nullCheck = true)
    }

    private fun isCheck(cd: CompileData, check: TIs, matches: Boolean): Map<String, Type> {
        val (name, type) = local(cd, check.expr) ?: return mapOf()
        val target = check.type.process(cd, Types.None).first
        if (type is Pointer && target is Pointer) return if (matches) unlessAliased(cd, check.expr, mapOf(name to target)) else mapOf()
        if (type !is Union) return mapOf()
        val (fitting, rest) = type.members.partition { it === target || cd.types.isSubtype(it, target) }
        val narrowed = cd.types.union(if (matches) fitting else rest)
        return if (narrowed === Types.Never || narrowed === type) mapOf() else unlessAliased(cd, check.expr, mapOf(name to narrowed))
    }

    /**
     * [narrowings] of the variable [tok], or none when its address is taken (`addrof x`): the check then proves nothing
     * about later reads, because a write through the pointer could change it. A warning says how to keep the value.
     */
    private fun unlessAliased(cd: CompileData, tok: ITok, narrowings: Map<String, Type>, nullCheck: Boolean = false): Map<String, Type> {
        val name = (tok as? TNamed)?.name ?: return narrowings
        val at = cd.addressTaken[name] ?: return narrowings
        if (cd.warnedAliased.add(tok.location.toString()))
            cd.warn(tok.location, "This check doesn't narrow '$name', because its address is taken at line ${at.getLine()} " +
                "(`addrof $name`), so it could change through that pointer after the check. Check a copy instead: " +
                "`val ${name}Now = $name`, then check and use `${name}Now`" + (if (nullCheck) " (or `if val ${name}Now = $name do`)." else "."))
        return mapOf()
    }
}
