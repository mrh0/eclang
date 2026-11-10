package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.NonValueTok
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData

import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.ect2.*

/** `(A, B) => R` or `(A, B) => R throws E`: a function type. */
class TTypeCallSignature(location: Loc, val argTypes: List<ITok>, val returnType: ITok?) : NonValueTok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val returns = returnType?.process(cd, Types.None)?.first ?: Types.None
        val params = argTypes.map { it.process(cd, Types.None).first }
        // `=> R throws E` parses as a function returning the type `R throws E`: it is a function that can fail.
        val type: Type = if (returns is Result) cd.types.function(params, returns.value, returns.error) else cd.types.function(params, returns)
        return type to IRType(location, type)
    }

    override fun toString(): String {
        return "TTypeCallSignature"
    }
}
