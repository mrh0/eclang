package github.mrh0.eclang.context.state

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.types.EcTypeAny

class Imported(private val name: String, private val type: EcTypeAny) : IVar {
    override fun getName() = name
    override fun getType() = type
}