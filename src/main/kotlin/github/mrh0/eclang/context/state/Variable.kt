package github.mrh0.eclang.context.state

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.EcTypeAny
import github.mrh0.eclang.values.GsBase

class Variable(private val name: String, private val type: EcTypeAny, private var value: GsBase) : IVar {
    override fun getName() = name
    override fun getType() = type
    override fun getValue(location: Loc) = value
    override fun setValue(location: Loc, value: GsBase) {
        this.value = value
    }
}