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
import github.mrh0.eclang.ir.accessor.IRSlice
import github.mrh0.ect2.ArrayType
import github.mrh0.ect2.IntLiteral
import github.mrh0.ect2.Pointer
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * `x[i]`: an element of an array, C array, String or pointer. Arrays and Strings check i against their length
 * (unless compiled with `--no-bounds-check`); `x.data[i]` skips the check. `x[a..<b]` and `x[a..=b]` slice an
 * array or String: the result shares x's memory.
 */
class TAccessor(location: Loc, val expr: ITok, val index: ITok) : Tok(location), IAssignable {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> = access(cd).let { (type, ir) -> type to ir }

    override fun place(cd: CompileData): Place {
        if (index.unnested() is TRange) throw EcError(location, "Can't assign to a slice. Assign its elements one by one, e.g. `a[i] = x`.")
        val (type, ir, base) = access(cd)
        val readOnly = if (base === Types.String)
            "A String's bytes can't be changed in place (a literal's bytes are read-only). Build a new String instead, e.g. with `\"\${...}\"` or `concat(a, b)`." else null
        return Place(type, ir, readOnly)
    }

    /** The element's type, the access, and the type of the indexed value. */
    private fun access(cd: CompileData): Triple<Type, IIR, Type> {
        val (t, ir) = cd.usable(location, expr.process(cd, Types.None), "`[]`")
        val range = index.unnested() as? TRange
        if (range != null) return slice(cd, t, ir, range)
        val (it, iir) = index(cd, index, "index")
        val result = when {
            t is ArrayType && !t.isC -> t.element to IRAccessorArray(location, ir, iir, t, cd.boundsChecks)
            t is ArrayType -> t.element to IRAccessorCArray(location, ir, iir)
            t === Types.String -> Types.Char to IRAccessorArray(location, ir, iir, t, cd.boundsChecks)
            t is Pointer && t.target !== Types.Any -> t.target to IRAccessorCArray(location, ir, iir)
            else -> throw EcError(location, "Can't index a $t with `[]`.")
        }
        return Triple(result.first, result.second, t)
    }

    private fun slice(cd: CompileData, t: Type, ir: IIR, range: TRange): Triple<Type, IIR, Type> {
        if (t !== Types.String && (t !is ArrayType || t.isC))
            throw EcError(location, "Only arrays and Strings can be sliced, but this is a $t.${if (t is ArrayType) " A C array has no length; slice the array it came from instead." else ""}")
        val from = index(cd, range.from, "start of the slice")
        val to = index(cd, range.to, "end of the slice")
        val (f, e) = (from.first as? IntLiteral)?.value to (to.first as? IntLiteral)?.value
        if (f != null && e != null && f > e + (if (range.inclusive) 1 else 0))
            throw EcError(location, "This slice starts after it ends ($f > $e).")
        return Triple(t, IRSlice(location, ir, from.second, to.second, range.inclusive, t, cd.boundsChecks), t)
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
