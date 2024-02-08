package github.mrh0.eclang.context.state

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.types.EcTypeAny
import github.mrh0.eclang.values.GsBase
import github.mrh0.eclang.values.GsGlobalFunction

class GlobalFunctionReference(private val name: String, private val type: EcTypeAny, val ref: GsGlobalFunction) : IVar {
    override fun getName() = name
    override fun getType() = type
    override fun getValue(location: Loc): GsBase = ref
    override fun setValue(location: Loc, value: GsBase) = throw EcError(location, "Cannot re-assign a global function")
}