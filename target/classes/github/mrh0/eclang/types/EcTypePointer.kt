package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcError

class EcTypePointer private constructor(val wrapped: EcType) : EcType(getPointerId(wrapped), if (wrapped == EcTypeAny) EcTypeAny else of(EcTypeAny)) {
    companion object {
        private fun getPointerId(wrapped: EcType) = "Pointer($wrapped)"

        fun of(wrapped: EcType): EcType = ALL_TYPES.getOrPut(getPointerId(wrapped)) { EcTypePointer(wrapped) }
    }

    override fun isReferenceType(): Boolean = true
}