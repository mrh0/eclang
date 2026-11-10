package github.mrh0.eclang.ast.token

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.ect2.Type

/**
 * `unsafe expr` or `unsafe do` + a block: [body], compiled with operations allowed that the compiler can't check
 * (`cd.isUnsafe`), such as `new (<T>)[n]` in a generic function. It changes nothing in the C.
 */
class TUnsafe(location: Loc, val body: ITok, private val isBlock: Boolean) : Tok(location) {
    override fun pointerFlow(): PointerFlow = if (isBlock) PointerFlow.NotAValue else PointerFlow.From(body)

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        cd.unsafeDepth++
        val result = try { body.process(cd, hint) } finally { cd.unsafeDepth-- }
        if (isBlock && (result.second as? IRBlock)?.diverges == true) cd.ctx().setReturned()
        return result
    }

    override fun toString() = "TUnsafe($body)"

    companion object {
        /** [statement], to be compiled later (deferred), still unsafe if it is written inside `unsafe` (`unsafe defer destroy p`). */
        fun keep(cd: CompileData, location: Loc, statement: ITok): ITok = if (cd.isUnsafe) TUnsafe(location, statement, isBlock = true) else statement
    }
}
