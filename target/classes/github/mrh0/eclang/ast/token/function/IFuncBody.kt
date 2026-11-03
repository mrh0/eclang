package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.ast.token.TBlock

interface IFuncBody {
    fun getBody(): TBlock = throw NotImplementedError()
}