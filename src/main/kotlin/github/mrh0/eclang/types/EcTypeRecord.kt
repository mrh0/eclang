package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc

class EcTypeRecord(val name: String) : EcType("Record") {
    override fun accepts(location: Loc, type: EcType): Boolean = type == this

    override fun isReferenceType(): Boolean = true
}