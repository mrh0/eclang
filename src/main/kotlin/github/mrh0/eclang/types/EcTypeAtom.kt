package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.values.GsAtom
import github.mrh0.eclang.values.GsBase

object EcTypeAtom : EcTypeAny("Gs", "Atom") {
    override fun construct(location: Loc, value: Any): GsBase = value as GsAtom
}