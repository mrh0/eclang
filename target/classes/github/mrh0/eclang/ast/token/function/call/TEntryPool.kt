package github.mrh0.eclang.ast.token.function.call

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.ect2.Type

/**
 * The pool that was current when the function was called, which `keep(x)` copies into (see [Builtins]): it outlives every
 * `pool do` block of the function, and the rounds of its loops. It only stands in a call's pools, for Lifetimes, which
 * counts it as the caller's pool ([github.mrh0.eclang.context.Lifetimes.POOL_SOURCE]) and never as local memory.
 */
class TEntryPool(location: Loc) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> = cd.types.pool to cd.entryPool(location)

    override fun pointerFlow(): PointerFlow = PointerFlow.Nothing

    override fun toString() = "TEntryPool"
}
