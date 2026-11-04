package github.mrh0.eclang.ast.token

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.ect2.Type

/** A value already compiled: [type] and C [ir] (a temporary, or the receiver of `q?.f()`). */
internal class TFixed(location: Loc, private val type: Type, private val ir: IIR) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> = type to ir
}
