package github.mrh0.eclang.ast.token.statement

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.NonValueTok
import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.branch.TStatementIf
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRDebugFlag
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * `debug statement` or `debug do` + a block ([body], a block either way): code only a debug build runs, as
 * `if (__EC_DEBUG) { ... }`, where `__EC_DEBUG` is 0 in a release build (`--release`). It is compiled and checked the same way
 * in both, as an `if` whose condition isn't known: so what it defines, and what it proves (`debug assert x != null`), stays
 * inside it, and code after it can't rely on it having run. See CTypes.useDebugFlag.
 */
class TDebug(location: Loc, private val body: ITok) : NonValueTok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val stack = cd.ctx().stack
        stack.debugDepth++
        try {
            return TStatementIf(location, listOf(Flag(location)), listOf(body), null).process(cd, hint)
        } finally {
            stack.debugDepth--
        }
    }

    /** Whether this is a debug build, as the condition of the `if`. */
    class Flag(location: Loc) : Tok(location) {
        override fun pointerFlow(): PointerFlow = PointerFlow.Nothing
        override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> = Types.Bool to IRDebugFlag(location, cd.release)
        override fun toString() = "debug"
    }

    override fun toString() = "TDebug($body)"
}
