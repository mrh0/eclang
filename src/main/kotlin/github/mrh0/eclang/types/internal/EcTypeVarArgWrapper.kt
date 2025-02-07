package github.mrh0.eclang.types.internal

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.EcType

class EcTypeVarArgWrapper(private val defType: EcType) : EcType("VarArgWrapper") {
    override fun toString() = "VAW($defType)"
    override fun accepts(location: Loc, type: EcType): Boolean {
        return type.accepts(location, type)
    }

    fun getContained() = defType

    override fun isReferenceType(): Boolean = defType.isReferenceType()
}