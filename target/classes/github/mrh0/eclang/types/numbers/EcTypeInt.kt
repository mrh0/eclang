package github.mrh0.eclang.types.numbers

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.values.numbers.GsInt

object EcTypeInt : EcTypeNumber("Gs", "Int") {
    override fun getJavaClass(location: Loc) = Int::class.java
    override fun construct(location: Loc, value: Any) = GsInt(value as Int)
}