package github.mrh0.eclang.context.state

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.EcTypeAny
import github.mrh0.eclang.values.GsBase

interface IVar {
    fun getValue(location: Loc): GsBase
    fun setValue(location: Loc, value: GsBase)
    fun getName(): String
    fun getType(): EcTypeAny
}