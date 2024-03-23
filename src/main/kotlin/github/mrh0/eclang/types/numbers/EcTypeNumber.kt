package github.mrh0.eclang.types.numbers

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.EcType

abstract class EcTypeNumber(namespace: String, identifier: String) : EcType(namespace, identifier) {
    override fun accepts(location: Loc, type: EcType): Boolean = type is EcTypeNumber
}