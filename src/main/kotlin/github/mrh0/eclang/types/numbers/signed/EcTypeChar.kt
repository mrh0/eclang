package github.mrh0.eclang.types.numbers.signed

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.EcType

object EcTypeChar : EcTypeSignedInteger("Char") {
    override fun accepts(location: Loc, type: EcType): Boolean = type == this
}