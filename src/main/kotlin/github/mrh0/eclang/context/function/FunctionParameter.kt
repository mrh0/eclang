package github.mrh0.eclang.context.function

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.types.EcType

@Deprecated("Use ParameterTemplate")
class FunctionParameter(val name: String, val type: EcType, val def: ITok?, val varArg: Boolean = false) {
    override fun toString() = "FP($name: $type, ${def != null}, ${varArg})"
}