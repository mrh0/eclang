package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.function.FunctionParameter

abstract class TParameter(location: Loc, val name: String) : Tok(location) {
    override fun toString() = "TParameter($name)"

    abstract fun toFunctionParameter(cd: CompileData): FunctionParameter
}