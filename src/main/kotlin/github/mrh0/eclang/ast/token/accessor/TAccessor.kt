package github.mrh0.eclang.ast.token.accessor

import github.mrh0.eclang.ast.IAssignable
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Place
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.op.Ops
import github.mrh0.eclang.ast.token.op.TRange
import github.mrh0.eclang.ast.unnested
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.accessor.IRAccessorArray
import github.mrh0.eclang.ir.accessor.IRAccessorCArray
import github.mrh0.eclang.ir.accessor.IRAccessorFixed
import github.mrh0.eclang.ir.accessor.IRFixedView
import github.mrh0.eclang.ir.accessor.IRSlice
import github.mrh0.eclang.ir.data.IRNumber
import github.mrh0.ect2.ArrayType
import github.mrh0.ect2.FixedArray
import github.mrh0.ect2.IntLiteral
import github.mrh0.ect2.Pointer
import github.mrh0.ect2.Tuple
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * `x[i]`: an element of an array, C array, String or pointer. Arrays and Strings check i against their length
 * (unless compiled with `--no-bounds-check`); `x.data[i]` skips the check. `x[a..<b]` and `x[a..=b]` slice an
 * array or String; either end may be left out, and a slice without an end goes up to the end of x: the result shares x's memory.
 * A `T[N]` holds its elements itself: `a[i]` is part of a, and a slice of it, an array, points into a.
 */
class TAccessor(location: Loc, val expr: ITok, val index: ITok) : Tok(location), IAssignable {
    /** Whether the last compiled access read an element of a `T[N]`, which is in x's own memory (see Lifetimes). */
    var inValue = false
        private set

    /** Whether the last compiled access was a slice of a `T[N]`: an array pointing into x (see Lifetimes). */
    var viewsValue = false
        private set

    /** Whether the element may be at any address, being inside a `packed` record. */
    var unaligned = false
        private set

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> = access(cd, expr.process(cd, Types.None)).let { (type, ir) -> type to ir }

    override fun place(cd: CompileData): Place {
        if (index.unnested() is TRange) throw EcError(location, "Can't assign to a slice. Assign its elements one by one, e.g. `a[i] = x`.")
        val e = expr.unnested()
        val owner = (e as? IAssignable)?.place(cd)
        val (type, ir, base) = access(cd, owner?.let { it.type to it.ir } ?: expr.process(cd, Types.None))
        val readOnly = when {
            base === Types.String -> "A String's bytes can't be changed in place (a literal's bytes are read-only). Build a new String instead, e.g. with `\"\${...}\"` or `concat(a, b)`."
            base is FixedArray -> if (owner != null) owner.readOnly else "This array is a temporary value, so changing an element would have no effect. Store it in a `var` first."
            else -> null
        }
        return Place(type, ir, readOnly)
    }

    /** The element's type, the access, and the type of the indexed value, [value]. */
    private fun access(cd: CompileData, value: Pair<Type, IIR>): Triple<Type, IIR, Type> {
        val (written, ir) = cd.usable(location, value, "`[]`")
        val t = Ops.base(written)
        inValue = t is FixedArray
        viewsValue = false
        unaligned = t is FixedArray && inPacked(expr)
        val range = index.unnested() as? TRange
        if (range != null) return slice(cd, t, ir, range)
        val (it, iir) = index(cd, index, "index")
        val result = when {
            t is FixedArray -> {
                val literal = (it as? IntLiteral)?.value
                if (literal != null && literal >= t.size) throw EcError(location, "Index $literal is out of bounds: a $t has ${t.size} element(s), so the last index is ${t.size - 1}.")
                t.element to IRAccessorFixed(location, ir, iir, t, cd.boundsChecks && literal == null)
            }
            t is ArrayType && !t.isC -> t.element to IRAccessorArray(location, ir, iir, t, cd.boundsChecks)
            t is ArrayType -> t.element to IRAccessorCArray(location, ir, iir)
            t === Types.String -> Types.Char to IRAccessorArray(location, ir, iir, t, cd.boundsChecks)
            t is Pointer && t.target !== Types.Any -> t.target to IRAccessorCArray(location, ir, iir)
            t is Tuple -> throw EcError(location, "A tuple's parts have names, not indexes: write `.first`, `.second`, and so on, or take it apart with `val (a, b) = t`.")
            else -> throw EcError(location, "Can't index a $t with `[]`.")
        }
        return Triple(result.first, result.second, t)
    }

    private fun slice(cd: CompileData, t: Type, ir: IIR, range: TRange): Triple<Type, IIR, Type> {
        if (t is FixedArray) {
            checkStored(location, expr, t, "sliced")
            checkViewable(location, expr, t, "A slice")
            viewsValue = true
            val array = cd.types.array(t.element)
            return slice(cd, array, IRFixedView(location, ir, array, t), range)
        }
        if (t !== Types.String && (t !is ArrayType || t.isC))
            throw EcError(location, "Only arrays and Strings can be sliced, but this is a $t.${if (t is ArrayType) " A C array has no length; slice the array it came from instead." else ""}")
        val from = range.from?.let { index(cd, it, "start of the slice") } ?: cd.types.intLiteral(0).let { it to IRNumber(location, 0, it) }
        val to = range.to?.let { index(cd, it, "end of the slice") }
        val (f, e) = (from.first as? IntLiteral)?.value to (to?.first as? IntLiteral)?.value
        if (f != null && e != null && f > e + (if (range.inclusive) 1 else 0))
            throw EcError(location, "This slice starts after it ends ($f > $e).")
        return Triple(t, IRSlice(location, ir, from.second, to?.second, range.inclusive, t, cd.boundsChecks), t)
    }

    private fun index(cd: CompileData, tok: ITok, what: String): Pair<Type, IIR> {
        val (it, iir) = cd.usable(location, tok.process(cd, Types.Size), what)
        if (!Ops.isInteger(it)) throw EcError(location, "The $what must be a whole number, but this is a $it.")
        if (it is IntLiteral && it.value < 0) throw EcError(location, "The $what can't be negative.")
        return it to iir
    }

    override fun toString(): String {
        return "TAccessor($expr, $index)"
    }
}
