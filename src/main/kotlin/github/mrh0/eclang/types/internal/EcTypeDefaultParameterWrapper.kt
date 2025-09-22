package github.mrh0.eclang.types.internal

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypePointer

class EcTypeDefaultParameterWrapper private constructor(private val defType: EcType) : EcType(getDefaultWrapperId(defType), defType) {
    companion object {
        private fun getDefaultWrapperId(wrapped: EcType) = "DefaultWrapper($wrapped)"

        fun of(wrapped: EcType): EcType = ALL_TYPES.getOrPut(getDefaultWrapperId(wrapped)) { EcTypeDefaultParameterWrapper(wrapped) }
    }
    override fun accepts(location: Loc, type: EcType): Boolean {
        return type.accepts(location, type)
    }

    fun getContained() = defType

    override fun isReferenceType(): Boolean = defType.isReferenceType()
}