package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc

class EcTypeDefaultWrapper(private val wrappedType: EcType) : EcType("IEc", "DefaultWrapper") {
    override fun toString() = "DW($wrappedType)"
    override fun accepts(location: Loc, type: EcType): Boolean {
        return type.accepts(location, type)
    }

    override fun expand(): List<EcType> = listOf(wrappedType, EcTypeNone)
}