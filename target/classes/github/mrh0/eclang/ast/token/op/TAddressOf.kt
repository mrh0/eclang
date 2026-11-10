package github.mrh0.eclang.ast.token.op

import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.TExprNest
import github.mrh0.eclang.ast.token.accessor.TAccessorNamed
import github.mrh0.eclang.ast.token.accessor.alignedAnywhere
import github.mrh0.eclang.ast.token.accessor.inPacked
import github.mrh0.eclang.ast.token.function.call.TExprCall
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.Lifetimes
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRComma
import github.mrh0.eclang.ir.data.IRAddressOf
import github.mrh0.eclang.ir.data.IRAddressOfKept
import github.mrh0.eclang.output.c.CTypes
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types
import github.mrh0.ect2.Union

/**
 * `addrof x`: a pointer to x. `addrof x!` points at a nullable x as if it were set, for a C out-parameter: it may only
 * be an argument of a `declare fn` (C fills x in, and x is still a `T?` afterwards). Anywhere else, the `@@T` would read
 * x as never null, although x may be (or later be set to) null.
 */
class TAddressOf(location: Loc, val expr: ITok) : Tok(location) {
    override fun pointerFlow(): PointerFlow = PointerFlow.Nothing // Lifetimes follows `addrof` itself (Lifetimes.place)

    /** Set by a call before it compiles its arguments: this is one of them, so `addrof x!` may be an out-parameter. */
    var isArgument = false

    /** Whether the last compile was `addrof x!` of a nullable x: the call must be of a `declare fn` (see [TExprCall]). */
    var isOutParameter = false
        private set

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        var inner = expr
        while (inner is TExprNest) inner = inner.expr
        isOutParameter = inner is TCastNotNull
        if (inner is TCastNotNull) {
            if (!isArgument) throw EcError(location, OUT_PARAMETER_ONLY)
            val (t, ir) = inner.expr.process(cd, Types.None)
            if (t is Union && !CTypes.isPlain(t)) throw EcError(location, "`addrof x!` points at a nullable value as if it were set, but a $t holds a tag next to its value, so a pointer to the ${cd.types.nonNull(t)} can't reach it. Use a `var` of type ${cd.types.nonNull(t)} instead, and give it its value after the call.")
            return cd.types.pointer(cd.types.nonNull(t)) to IRAddressOf(location, ir)
        }
        val (t, ir) = cd.usable(location, expr.process(cd, Types.None), "`addrof`")
        if (Ops.isLiteral(t)) throw EcError(location, "Can't take the address of a literal; store it in a variable first.")
        if (inner is TExprCall) throw EcError(location, "Can't take the address of what a call returns: it isn't stored anywhere. Store it in a variable first: `val x = ...`, then `addrof x`.")
        if (inner is TAccessorNamed && inner.bitFieldOf != null)
            throw EcError(location, "Can't take the address of bit-field '${inner.name}': it doesn't start at a whole byte. Copy it into a variable first.")
        if (inPacked(inner) && !alignedAnywhere(t))
            throw EcError(location, "Can't take the address of this $t: it is inside a `packed` record, so it may be at any address, where C can't use a pointer to it. Copy it into a variable first.")
        if (Lifetimes.isLiteral(inner)) {
            // A literal whose values went into temporaries is a comma expression, which C can't take the address of.
            val storage = cd.literalStorage(location, t) ?: if (ir is IRComma) cd.declareTemp(location) { t } else null
            storage?.let { return cd.types.pointer(t) to IRAddressOfKept(location, it, ir) }
        }
        return cd.types.pointer(t) to IRAddressOf(location, ir)
    }

    override fun toString(): String {
        return "TAddressOf($expr)"
    }
}

/** Why `addrof x!` is only allowed as an argument of a `declare fn`. */
internal const val OUT_PARAMETER_ONLY = "`addrof x!` points at x as if it could never be null, which is only safe for a C function (`declare fn`) that fills x in: pass it straight to one. Elsewhere, use `addrof x`, a pointer to the nullable x, or check x first (`if x != null do`) and take the address of a copy."
