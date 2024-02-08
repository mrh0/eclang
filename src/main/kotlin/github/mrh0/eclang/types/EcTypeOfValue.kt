package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.values.GsBase

class EcTypeOfValue(private val of: GsBase) : EcTypeAny("Gs", "OfValue") {
    override fun toString() = "$of"

    override fun accepts(location: Loc, value: GsBase) = value == of
}