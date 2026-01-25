package github.mrh0.eclang.context.fn2

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.types.EcType

data class ParameterTemplate(val name: String, val baseType: EcType, val def: ITok? = null, val isVarArg: Boolean = false, val isGeneric: Boolean = false, val isVolatile: Boolean = false, val isConst: Boolean = false) {
    override fun toString() = name
}