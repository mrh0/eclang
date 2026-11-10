package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.NonValueTok
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.ect2.Type

class TParameters(location: Loc, val args: MutableList<TParameter>) : NonValueTok(location) {
    fun get() = args.map { it }

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> = throw NotImplementedError()

    override fun toString(): String {
        return "TParameters($args)"
    }
}
