package github.mrh0.eclang.types.numbers

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.EcType

abstract class EcTypeNumber(identifier: String) : EcType(identifier) {
    override fun cast(location: Loc, to: EcType): EcType {
        if (to is EcTypeNumber) return to
        return super.cast(location, to)
    }
}