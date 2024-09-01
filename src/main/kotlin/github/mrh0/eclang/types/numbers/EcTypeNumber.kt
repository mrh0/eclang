package github.mrh0.eclang.types.numbers

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.EcType

abstract class EcTypeNumber(identifier: String) : EcType(identifier) {
    override fun accepts(location: Loc, type: EcType): Boolean = type is EcTypeNumber
}