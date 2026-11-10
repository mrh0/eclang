package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IRConvert
import github.mrh0.eclang.output.c.CTypes
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** `x !as T` (or `x as unsafe T`): a plain C cast (or a view of the same bytes, String ↔ Char[]). Nothing is checked; use it only when `as` can't do it. */
class TCastUnsafe(location: Loc, val expr: ITok, val type: ITok) : Tok(location) {
    override fun pointerFlow(): PointerFlow = PointerFlow.From(expr)

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val to = cd.storable(location, type.process(cd, Types.None).first, "`!as`")
        val (from, ir) = cd.usable(location, expr.process(cd, Types.None), "`!as`")
        if (!CTypes.canCastUnsafe(from, to)) throw EcError(location, "Can't convert $from to $to with `!as`: it is a plain C cast, which only " +
            "converts numbers and pointers, and ${listOf(from, to).first { !CTypes.isScalar(it) }} is a struct in C. " +
            "Use `as` for a checked conversion, convert its parts (a field, `.data` of an array), or cast a pointer to it: `(addrof x) !as @T`. " +
            "(A String and a Char[] or UChar[] can be cast into each other: they share the same bytes.)")
        return to to IRConvert(location, ir, from, to, IRConvert.Mode.UNSAFE)
    }

    override fun toString(): String {
        return "TCastUnsafe($expr, $type)"
    }
}
