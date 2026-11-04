package github.mrh0.eclang.ast.token.op

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.TExprNest
import github.mrh0.eclang.ast.token.accessor.TAccessorNamed
import github.mrh0.eclang.ast.token.accessor.alignedAnywhere
import github.mrh0.eclang.ast.token.accessor.inPacked
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRAddressOf
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** `addrof x`: a pointer to x. `addrof x!` points at a nullable x as if it were set (for C out-parameters). */
class TAddressOf(location: Loc, val expr: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        var inner = expr
        while (inner is TExprNest) inner = inner.expr
        if (inner is TCastNotNull) {
            val (t, ir) = inner.expr.process(cd, Types.None)
            return cd.types.pointer(cd.types.nonNull(t)) to IRAddressOf(location, ir)
        }
        val (t, ir) = cd.usable(location, expr.process(cd, Types.None), "`addrof`")
        if (Ops.isLiteral(t)) throw EcError(location, "Can't take the address of a literal; store it in a variable first.")
        if (inner is TAccessorNamed && inner.bitFieldOf != null)
            throw EcError(location, "Can't take the address of bit-field '${inner.name}': it doesn't start at a whole byte. Copy it into a variable first.")
        if (inPacked(inner) && !alignedAnywhere(t))
            throw EcError(location, "Can't take the address of this $t: it is inside a `packed` record, so it may be at any address, where C can't use a pointer to it. Copy it into a variable first.")
        return cd.types.pointer(t) to IRAddressOf(location, ir)
    }

    override fun toString(): String {
        return "TAddressOf($expr)"
    }
}
