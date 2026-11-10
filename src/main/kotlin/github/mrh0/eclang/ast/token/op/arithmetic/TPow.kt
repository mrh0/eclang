package github.mrh0.eclang.ast.token.op.arithmetic

import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.op.Ops
import github.mrh0.eclang.ast.token.op.arithmetic
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.arithmetic.pow.IRPowDoubleDouble
import github.mrh0.eclang.ir.arithmetic.pow.IRPowInt
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * `a ** b`. On whole numbers it is a whole number of their common type, computed exactly (`3L ** 39`); a negative
 * exponent gives 0 (1 ** n is 1, -1 ** n is ±1). Otherwise it is a Double, from C's `pow` (link with `-lm`).
 */
class TPow(location: Loc, val left: ITok, val right: ITok) : Tok(location) {
    override fun pointerFlow(): PointerFlow = PointerFlow.Nothing // a number or Bool

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        var type: Type = Types.Double
        return arithmetic(cd, location, "**", left, right, ::power, { a, b -> Math.pow(a, b) },
            resultType = { common -> (if (Ops.isInteger(common)) common else Types.Double).also { type = it } }) { l, r ->
            if (type === Types.Double) IRPowDoubleDouble(location, l, r) else IRPowInt(location, l, r, type)
        }
    }

    /** `a ** b` on two whole-number literals, exactly (an error when it doesn't fit a Long). */
    private fun power(base: Long, exponent: Long): Long {
        if (exponent < 0) throw ArithmeticException("a whole-number power needs an exponent of 0 or more; write `$base.0 ** $exponent` for a fraction")
        if (base == -1L) return if (exponent % 2 == 0L) 1 else -1
        if (base == 0L || base == 1L) return if (exponent == 0L) 1 else base
        // Any other base overflows a Long within 64 multiplications.
        var result = 1L
        try {
            repeat(exponent.coerceAtMost(64).toInt()) { result = Math.multiplyExact(result, base) }
        } catch (e: ArithmeticException) {
            throw ArithmeticException("the result is too big for a Long")
        }
        return result
    }

    override fun toString(): String {
        return "TPow($left, $right)"
    }
}
