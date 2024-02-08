package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.values.GsBase
import github.mrh0.eclang.values.GsBoxed

object EcTypeBoxed : EcTypeAny("Gs", "Boxed") {
    override fun toString() = "Boxed"
    override fun construct(location: Loc, value: Any): GsBase = GsBoxed(value)
}