package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.function.Parameter
import github.mrh0.eclang.ir.IIR
import github.mrh0.ect2.Type

abstract class TParameter(location: Loc, val name: String) : Tok(location) {
    abstract fun toFunctionParameter(cd: CompileData): Parameter

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> = throw NotImplementedError("Parameters are read with toFunctionParameter")
}
