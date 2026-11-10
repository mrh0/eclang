package github.mrh0.eclang.ir

import github.mrh0.eclang.ir.arithmetic.add.IRAdd
import github.mrh0.eclang.ir.arithmetic.div.IRDiv
import github.mrh0.eclang.ir.arithmetic.div.IRMod
import github.mrh0.eclang.ir.arithmetic.mul.IRMul
import github.mrh0.eclang.ir.arithmetic.negate.IRNegate
import github.mrh0.eclang.ir.arithmetic.sub.IRSub
import github.mrh0.eclang.ir.compare.equals.IREquals
import github.mrh0.eclang.ir.compare.greater.IRGreaterOrEquals
import github.mrh0.eclang.ir.compare.greater.IRGreaterThan
import github.mrh0.eclang.ir.compare.less.IRLessOrEquals
import github.mrh0.eclang.ir.compare.less.IRLessThan
import github.mrh0.eclang.ir.compare.not_equals.IRNotEquals
import github.mrh0.eclang.ir.data.IRBool
import github.mrh0.eclang.ir.data.IRNumber
import github.mrh0.eclang.ir.data.IRSizeOf
import github.mrh0.eclang.ir.logical.and.IRAnd
import github.mrh0.eclang.ir.logical.not.IRNotBool
import github.mrh0.eclang.ir.logical.or.IROr
import github.mrh0.eclang.ir.logical.xor.IRXOr

/**
 * The value of compiled code when it is known while compiling, for `const assert` and for an `assert` that can't fail (see
 * Assertions): Bools, numbers (`Long` or `Double`), comparisons, `and`/`or`/`not` and arithmetic on them, and constants.
 * `sizeof`, `alignof` and `offsetof` only C knows, so code using them is [InC], a C constant expression (`_Static_assert` can
 * check it). Anything else, such as a variable or a call, isn't constant: [eval] gives null.
 */
object ConstFold {
    /** A value C knows when it compiles, but eclang doesn't. */
    object InC

    fun eval(ir: IIR): Any? = when (ir) {
        is IRBool -> ir.value
        is IRNumber -> ir.value.let { if (it is Double || it is Float) it.toDouble() else it.toLong() }
        is IRConstantRef -> eval(ir.value)
        is IRExprNest -> eval(ir.expr)
        // A lossless conversion of a number keeps its value (a narrowing one, `as`, isn't folded).
        is IRConvert -> if (ir.mode == IRConvert.Mode.IMPLICIT) eval(ir.expr) else null
        is IRSizeOf -> InC
        is IRNotBool -> eval(ir.expr).let { if (it is Boolean) !it else if (it === InC) InC else null }
        is IRAnd -> logical(ir.left, ir.right) { a, b -> a && b }
        is IROr -> logical(ir.left, ir.right) { a, b -> a || b }
        is IRXOr -> logical(ir.left, ir.right) { a, b -> a != b }
        is IREquals -> compare(ir.left, ir.right) { it == 0 }
        is IRNotEquals -> compare(ir.left, ir.right) { it != 0 }
        is IRLessThan -> compare(ir.left, ir.right) { it < 0 }
        is IRLessOrEquals -> compare(ir.left, ir.right) { it <= 0 }
        is IRGreaterThan -> compare(ir.left, ir.right) { it > 0 }
        is IRGreaterOrEquals -> compare(ir.left, ir.right) { it >= 0 }
        is IRAdd -> arithmetic(ir.left, ir.right, Long::plus, Double::plus)
        is IRSub -> arithmetic(ir.left, ir.right, Long::minus, Double::minus)
        is IRMul -> arithmetic(ir.left, ir.right, Long::times, Double::times)
        is IRDiv -> arithmetic(ir.left, ir.right, { a, b -> if (b == 0L) null else a / b }, Double::div)
        is IRMod -> arithmetic(ir.left, ir.right, { a, b -> if (b == 0L) null else a % b }, Double::rem)
        is IRNegate -> eval(ir.expr).let { if (it is Long) -it else if (it is Double) -it else if (it === InC) InC else null }
        else -> null
    }

    /** A Bool that is known, or null. */
    fun bool(ir: IIR): Boolean? = eval(ir) as? Boolean

    private fun logical(left: IIR, right: IIR, op: (Boolean, Boolean) -> Boolean): Any? {
        val a = eval(left) ?: return null
        val b = eval(right) ?: return null
        return if (a is Boolean && b is Boolean) op(a, b) else InC
    }

    private fun compare(left: IIR, right: IIR, test: (Int) -> Boolean): Any? {
        val a = eval(left) ?: return null
        val b = eval(right) ?: return null
        if (a === InC || b === InC) return InC
        return when {
            a is Long && b is Long -> test(a.compareTo(b))
            a is Number && b is Number -> test(a.toDouble().compareTo(b.toDouble()))
            a is Boolean && b is Boolean -> test(a.compareTo(b))
            else -> null
        }
    }

    private fun arithmetic(left: IIR, right: IIR, longs: (Long, Long) -> Long?, doubles: (Double, Double) -> Double): Any? {
        val a = eval(left) ?: return null
        val b = eval(right) ?: return null
        if (a === InC || b === InC) return InC
        return when {
            a is Long && b is Long -> longs(a, b)
            a is Number && b is Number -> doubles(a.toDouble(), b.toDouble())
            else -> null
        }
    }
}
