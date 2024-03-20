package github.mrh0.eclang.types.numbers

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.EcTypeAny

abstract class EcTypeNumber(namespace: String, identifier: String) : EcTypeAny(namespace, identifier) {
    override fun accepts(location: Loc, type: EcTypeAny): Boolean = type is EcTypeNumber
}