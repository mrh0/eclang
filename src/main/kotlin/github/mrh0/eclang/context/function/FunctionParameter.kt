package github.mrh0.eclang.context.function

import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.types.EcType

class FunctionParameter(val name: String, val type: EcType, val def: IIR?) {
    override fun toString(): String {
        return "FP($name: $type, ${def != null})"
    }
}