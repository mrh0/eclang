package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc

object EcTypeRecord : EcType("Record") {
    override fun accepts(location: Loc, type: EcType): Boolean = type == this
}