package github.mrh0.eclang.ast.token.op

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.arithmetic.IRCheckedArith
import github.mrh0.ect2.*

/** Typing rules shared by operators and array literals. */
object Ops {
    fun isLiteral(t: Type) = t is IntLiteral || t is FloatLiteral
    fun isNumeric(t: Type): Boolean = isLiteral(t) || (t is Prim && t.isNumber) || (t is Distinct && isNumeric(t.base))
    fun isInteger(t: Type): Boolean = t is IntLiteral || (t is Prim && t.isInteger) || (t is Distinct && isInteger(t.base))

    /** What a value of type [t] is underneath: the base of a `distinct` type, otherwise [t]. */
    fun base(t: Type) = if (t is Distinct) t.base else t

    /**
     * The type two values are combined as, e.g. `a + b` or the elements of `[a, b]`: the one the other converts
     * to without losing anything. A literal adapts to the other side (`c + 1` with `c: Char` is a Char).
     * Fails when neither fits the other, e.g. Int and UInt, with a message saying how to fix it.
     */
    fun common(cd: CompileData, location: Loc, what: String, a: Type, b: Type): Type {
        if (a === b) return a
        if (isLiteral(a) && isLiteral(b)) return common(cd, location, what, cd.types.concrete(a), cd.types.concrete(b))
        val types = cd.types
        val aToB = types.cost(a, b)
        val bToA = types.cost(b, a)
        return when {
            aToB != TypeContext.NO && (bToA == TypeContext.NO || aToB <= bToA) -> b
            bToA != TypeContext.NO -> a
            else -> throw EcError(location, "Can't combine $a and $b ($what): ${types.explain(b, a)}. Convert one side with `as`.")
        }
    }
}

/** `a op b` on numbers. When both sides are literals the result is computed now, so it can still adapt its type. */
internal fun arithmetic(
    cd: CompileData, location: Loc, op: String, left: github.mrh0.eclang.ast.ITok, right: github.mrh0.eclang.ast.ITok,
    intFold: ((Long, Long) -> Long)?, floatFold: ((Double, Double) -> Double)?, integerOnly: Boolean = false,
    /** The type both sides are converted to and the result has, given their common type (default: that type). */
    resultType: (Type) -> Type = { it },
    /** Added to the error when a side isn't a number, e.g. a hint to use `and` for Bools. */
    notNumberHint: (Type, Type) -> String = { _, _ -> "" },
    /** Added to the error when [integerOnly] and a side is a Float or Double, e.g. a hint to use `fmod`. */
    fractionHint: String = "",
    /** The operator, when its result is checked at runtime on whole numbers (see [RuntimeChecks]). */
    checked: String? = null,
    make: (github.mrh0.eclang.ir.IIR, github.mrh0.eclang.ir.IIR) -> github.mrh0.eclang.ir.IIR,
): Pair<Type, github.mrh0.eclang.ir.IIR> {
    val l = cd.usable(location, left.process(cd, Types.None), "left side of `$op`")
    val r = cd.usable(location, right.process(cd, Types.None), "right side of `$op`")
    val (lt, rt) = l.first to r.first
    if (!Ops.isNumeric(lt) || !Ops.isNumeric(rt)) throw EcError(location, "`$op` needs two ${if (integerOnly) "whole " else ""}numbers, but got $lt and $rt.${notNumberHint(lt, rt)}")
    if (integerOnly && (!Ops.isInteger(lt) || !Ops.isInteger(rt))) throw EcError(location, "`$op` needs two whole numbers, but got $lt and $rt.$fractionHint")
    try {
        if (lt is IntLiteral && rt is IntLiteral && intFold != null) {
            val v = intFold(lt.value, rt.value)
            return cd.types.intLiteral(v) to github.mrh0.eclang.ir.data.IRNumber(location, v, cd.types.intLiteral(v))
        }
        if (Ops.isLiteral(lt) && Ops.isLiteral(rt) && floatFold != null) {
            val v = floatFold(literalValue(lt), literalValue(rt))
            return cd.types.floatLiteral(v) to github.mrh0.eclang.ir.data.IRNumber(location, v, cd.types.floatLiteral(v))
        }
    } catch (e: ArithmeticException) {
        throw EcError(location, "`$op` on $lt and $rt: ${e.message}.")
    }
    val type = resultType(cd.types.concrete(Ops.common(cd, location, "`$op`", lt, rt)))
    val (lc, rc) = cd.expect(location, l, type, "left side of `$op`") to cd.expect(location, r, type, "right side of `$op`")
    if (checked != null && RuntimeChecks.needed(cd, checked, type, rt)) return type to IRCheckedArith(location, checked, Ops.base(type) as Prim, null, lc, rc)
    return type to make(lc, rc)
}

/**
 * Which whole-number operations are checked at runtime (see IRCheckedArith): `/` and `%` unless the divisor is a
 * constant other than 0 and -1, shifts by a count that isn't a constant, and, with `--overflow-check`, `+`, `-`, `*`.
 */
internal object RuntimeChecks {
    /** Whether `a op b` on [type], with a right side of type [right] (before conversion), needs a runtime check. */
    fun needed(cd: CompileData, op: String, type: Type, right: Type): Boolean {
        val whole = Ops.base(type)
        if (whole !is Prim || !whole.isInteger) return false
        val constant = (right as? IntLiteral)?.value
        return when (op) {
            "/", "%" -> cd.arithmeticChecks && (constant == null || constant == 0L || constant == -1L)
            "<<", ">>" -> cd.arithmeticChecks && constant == null
            "+", "-", "*" -> cd.overflowChecks
            else -> false
        }
    }
}

private fun literalValue(t: Type) = when (t) {
    is IntLiteral -> t.value.toDouble()
    is FloatLiteral -> t.value
    else -> error("not a literal")
}

/** Which kind of values a comparison compares, deciding the C code it becomes. */
internal enum class CompareKind { NUMBER, STRING, CSTRING, IDENTITY, RECORD }

/**
 * Checks that `a op b` compares two values of compatible types and converts both sides. Returns the kind of
 * comparison and the converted sides. [equality]: `==`/`!=`, which also work on Bool, atoms and pointers.
 */
internal fun comparison(
    cd: CompileData, location: Loc, op: String, l: Pair<Type, github.mrh0.eclang.ir.IIR>, r: Pair<Type, github.mrh0.eclang.ir.IIR>, equality: Boolean,
): Triple<CompareKind, github.mrh0.eclang.ir.IIR, github.mrh0.eclang.ir.IIR> {
    cd.usable(location, l, "left side of `$op`")
    cd.usable(location, r, "right side of `$op`")
    val (lt, rt) = l.first to r.first
    // A `distinct` value compares like its base, with a value of its own type or a literal.
    val distinct = lt as? Distinct ?: rt as? Distinct
    if (distinct != null && (lt === rt || cd.types.accepts(distinct, if (lt === distinct) rt else lt)))
        return comparison(cd, location, op, Ops.base(lt) to l.second, Ops.base(rt) to r.second, equality)
    fun atomish(t: Type) = t === Types.Atom || t is AtomLiteral || (t is Union && t.members.all { it === Types.Atom || it is AtomLiteral || it === Types.Null })
    return when {
        Ops.isNumeric(lt) && Ops.isNumeric(rt) -> {
            val type = cd.types.concrete(Ops.common(cd, location, "`$op`", lt, rt))
            Triple(CompareKind.NUMBER, cd.expect(location, l, type, "`$op`"), cd.expect(location, r, type, "`$op`"))
        }
        lt === Types.CString && rt === Types.CString -> Triple(CompareKind.CSTRING, l.second, r.second)
        lt === Types.String && rt === Types.String && equality -> Triple(CompareKind.STRING, l.second, r.second)
        equality && lt === Types.Bool && rt === Types.Bool -> Triple(CompareKind.IDENTITY, l.second, r.second)
        equality && atomish(lt) && atomish(rt) -> Triple(CompareKind.IDENTITY, l.second, r.second)
        equality && lt is Pointer && lt === rt -> Triple(CompareKind.IDENTITY, l.second, r.second)
        equality && (lt is Record || lt is Tuple || lt is FixedArray) && lt === rt -> {
            equalityProblem(lt)?.let {
                throw EcError(location, if (lt is Record) "`$op` can't compare $lt records: $it. Compare the fields you need yourself, e.g. `a.x == b.x`."
                    else if (lt is FixedArray) "`$op` can't compare $lt arrays: $it. Compare the elements you need yourself."
                    else "`$op` can't compare $lt tuples: $it. Compare the parts you need yourself, e.g. `a.first == b.first`.")
            }
            Triple(CompareKind.RECORD, l.second, r.second)
        }
        else -> throw EcError(location, "`$op` can't compare $lt with $rt.")
    }
}

/**
 * Why `==` can't compare two values of type [t] field by field, or null when it can: numbers, Bools, Chars,
 * atoms, Strings and CStrings (by content), pointers (by address) and records of those.
 */
internal fun equalityProblem(t: Type): String? = when {
    t is Distinct -> equalityProblem(t.base)
    t is Prim || t is Pointer || t is FunctionType || t is AtomLiteral -> null
    t is Union && t.members.all { it === Types.Null || it === Types.Atom || it is AtomLiteral } -> null
    t is Union && t.isNullable && t.members.size == 2 && t.members.any { it is Pointer || it === Types.CString } -> null
    t is Record && t.isUnion -> "${t.name} is a union, and which field it holds isn't known"
    t is Record && t.isExternal -> "${t.name} is a C type, which `==` can't look inside"
    t is Record -> t.fields.entries.firstNotNullOfOrNull { (name, field) -> equalityProblem(field)?.let { "field `$name`: $it" } }
    t is Tuple -> t.elements.firstNotNullOfOrNull { equalityProblem(it) }
    t is FixedArray -> equalityProblem(t.element)
    else -> "a $t can't be compared with `==` yet"
}

/** `x == null` / `x != null`, or null when neither side is `null`. */
internal fun nullComparison(
    cd: CompileData, location: Loc, l: Pair<Type, github.mrh0.eclang.ir.IIR>, r: Pair<Type, github.mrh0.eclang.ir.IIR>, negated: Boolean,
): Pair<Type, github.mrh0.eclang.ir.IIR>? {
    val (value, other) = when {
        r.first === Types.Null -> l to r
        l.first === Types.Null -> r to l
        else -> return null
    }
    if (other.first !== Types.Null) return null
    val type = value.first
    if (type === Types.Null) return Types.Bool to github.mrh0.eclang.ir.data.IRBool(location, !negated)
    if (type !is Union || !type.isNullable) {
        if (cd.function?.isUnionCopy == true) return Types.Bool to github.mrh0.eclang.ir.data.IRBool(location, negated)
        throw EcError(location, "A value of type $type can never be null, so this comparison is always ${if (negated) "true" else "false"}. To allow null, declare the type as `$type?`.")
    }
    return Types.Bool to github.mrh0.eclang.ir.IRIs(location, value.second, type, Types.Null, negated)
}

/** Checks [value] is a Bool (for `if`, `while`, `and`, ...), with a hint on how to get one. */
internal fun requireBool(cd: CompileData, location: Loc, what: String, value: Pair<Type, github.mrh0.eclang.ir.IIR>): github.mrh0.eclang.ir.IIR {
    val t = cd.usable(location, value, what).first
    if (t === Types.Bool) return value.second
    val hint = when {
        Ops.isNumeric(t) -> " Compare it, e.g. `x != 0`, or test it with `!!x`."
        t is Union && t.isNullable -> " Check it with `x != null`."
        else -> ""
    }
    throw EcError(location, "$what must be a Bool, but it is $t.$hint")
}

/**
 * Compiles [right] knowing [left]'s value: the right side of `a and b` only runs when a is true (and of `a or b`
 * when a is false), so it sees the narrowing that implies, and can't hoist a `try` out of the condition.
 */
internal fun shortCircuitRight(cd: CompileData, left: github.mrh0.eclang.ast.ITok, right: github.mrh0.eclang.ast.ITok, leftIs: Boolean): Pair<Type, github.mrh0.eclang.ir.IIR> {
    cd.ctx().push()
    try {
        github.mrh0.eclang.context.Narrowing.apply(cd, github.mrh0.eclang.context.Narrowing.of(cd, left, leftIs))
        return cd.noHoist("`try` can't be used on the right side of `and`/`or`; assign it to a variable first.") { right.process(cd, Types.None) }
    } finally {
        cd.ctx().pop()
    }
}
