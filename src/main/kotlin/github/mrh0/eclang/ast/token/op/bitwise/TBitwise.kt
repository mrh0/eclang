package github.mrh0.eclang.ast.token.op.bitwise

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.op.Ops
import github.mrh0.eclang.ast.token.op.arithmetic
import github.mrh0.eclang.ast.token.op.operands
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.bitwise.IRBitwise
import github.mrh0.eclang.ir.data.IRNumber
import github.mrh0.eclang.ast.token.op.RuntimeChecks
import github.mrh0.eclang.ir.arithmetic.IRCheckedArith
import github.mrh0.ect2.IntLiteral
import github.mrh0.ect2.Prim
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * `a & b`, `a | b`, `a ^ b` (and, or, xor of each bit) and `a << n`, `a >> n` (shift by n bits), on whole numbers.
 * A shift has the type of its left side; the count must be less than that type's number of bits.
 */
class TBitwise(location: Loc, private val op: String, private val left: ITok, private val right: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        if (op == "<<" || op == ">>") return shift(cd)
        val fold: (Long, Long) -> Long = when (op) {
            "&" -> { a, b -> a and b }
            "|" -> { a, b -> a or b }
            else -> { a, b -> a xor b }
        }
        val logical = mapOf("&" to "and", "|" to "or", "^" to "xor")[op]
        return arithmetic(cd, location, op, left, right, fold, null, integerOnly = true,
            notNumberHint = { l, r -> if (l === Types.Bool || r === Types.Bool) " For Bools use `$logical`." else "" },
        ) { l, r -> IRBitwise(location, op, l, r, null) }
    }

    private fun shift(cd: CompileData): Pair<Type, IIR> = operands(cd, location, left, right) { a, b ->
        shiftOf(cd, cd.usable(location, a, "left side of `$op`"), cd.usable(location, b, "right side of `$op`"))
    }

    private fun shiftOf(cd: CompileData, l: Pair<Type, IIR>, r: Pair<Type, IIR>): Pair<Type, IIR> {
        val (lt, rt) = l.first to r.first
        if (!Ops.isInteger(lt) || !Ops.isInteger(rt)) throw EcError(location, "`$op` needs two whole numbers, but got $lt and $rt.")
        val count = (rt as? IntLiteral)?.value
        if (count != null && count < 0) throw EcError(location, "Can't shift by a negative number of bits ($count).")

        if (lt is IntLiteral && count != null) {
            if (count > 62) throw EcError(location, "Shifting by $count bits is too far; a number has at most 64 bits.")
            val v = if (op == "<<") lt.value shl count.toInt() else lt.value shr count.toInt()
            if (op == "<<" && (v shr count.toInt()) != lt.value) throw EcError(location, "${lt.value} << $count is too big for a whole number.")
            return cd.types.intLiteral(v) to IRNumber(location, v, cd.types.intLiteral(v))
        }
        val shifted = cd.types.concrete(lt)
        val type = Ops.base(shifted) as Prim
        if (count != null && count >= type.bits)
            throw EcError(location, "Can't shift a $type by $count bits: it only has ${type.bits}. Convert it to a wider type first, e.g. `(x as Long) $op $count`.")
        val countType = cd.types.concrete(rt)
        if (RuntimeChecks.needed(cd, op, type, rt))
            return shifted to IRCheckedArith(location, op, type, Ops.base(countType) as Prim, cd.expect(location, l, shifted, "left side of `$op`"), cd.expect(location, r, countType, "right side of `$op`"))
        val ir = IRBitwise(location, op, cd.expect(location, l, shifted, "left side of `$op`"), cd.expect(location, r, countType, "right side of `$op`"),
            // C computes on at least an int, so a shifted small type is cut back to its own size.
            if (op == "<<" && type.bits < 32) type else null)
        return shifted to ir
    }

    override fun toString() = "TBitwise($left $op $right)"
}
