package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc

class EcTypeCArray private constructor(val arg: EcType) : EcType(getArrayId(arg)) {
    companion object {
        private fun getArrayId(wrapped: EcType) = "CArray($wrapped)"

        fun of(wrapped: EcType): EcType = ALL_TYPES.getOrPut(getArrayId(wrapped)) { EcTypeCArray(wrapped) }
    }

    override fun isReferenceType(): Boolean = true
}