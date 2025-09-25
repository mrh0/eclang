package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcError

data class EcTypeBoxed(val wrapped: EcType) : EcType(getBoxedId(wrapped)) {
    companion object {
        private fun getBoxedId(wrapped: EcType) = "Boxed($wrapped)"

        fun of(wrapped: EcType): EcType = ALL_TYPES.getOrPut(getBoxedId(wrapped)) { EcTypeBoxed(wrapped) }
    }

    override fun isReferenceType(): Boolean = true
}