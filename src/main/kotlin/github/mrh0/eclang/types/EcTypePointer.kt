package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcError

class EcTypePointer private constructor(val wrapped: EcType) : EcType(getPointerId(wrapped), wrapped) {
    companion object {
        private fun getPointerId(wrapped: EcType) = "Pointer($wrapped)"

        fun of(wrapped: EcType): EcType = ALL_TYPES.getOrPut(getPointerId(wrapped)) { EcTypePointer(wrapped) }
    }

    //override fun accepts(location: Loc, type: EcType): Boolean = if (type is EcTypePointer) wrapped.accepts(location, type.wrapped) else false

    override fun isReferenceType(): Boolean = true
}