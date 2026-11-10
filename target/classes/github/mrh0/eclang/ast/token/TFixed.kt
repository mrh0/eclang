package github.mrh0.eclang.ast.token

import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.ect2.Type

/** A value already compiled: [type] and C [ir] (a temporary, or the receiver of `q?.f()`). [from] is the expression it was compiled from, for Lifetimes. */
internal class TFixed(location: Loc, private val type: Type, private val ir: IIR, private val from: ITok?) : Tok(location) {
    override fun pointerFlow(): PointerFlow = PointerFlow.From(from)

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> = type to ir
}
