package github.mrh0.eclang.types

import github.mrh0.eclang.context.fn2.FunctionTemplate

class EcTypeFunctionReference private constructor(val ft: FunctionTemplate) : EcType(getFunctionRefId(ft)) {
    companion object {
        private fun getFunctionRefId(ft: FunctionTemplate) = "FunctionRef(${ft.namespace}:${ft.identifier})"

        fun of(ft: FunctionTemplate): EcType = ALL_TYPES.getOrPut(getFunctionRefId(ft)) { EcTypeFunctionReference(ft) }
    }

    override fun isReferenceType(): Boolean = true
}