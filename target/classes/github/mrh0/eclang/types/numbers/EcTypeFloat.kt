package github.mrh0.eclang.types.numbers

import github.mrh0.eclang.ast.Loc

object EcTypeFloat : EcTypeNumber("Gs", "Float") {
    override fun getJavaClass(location: Loc) = Double::class.java
    override fun construct(location: Loc, value: Any) = EcTypeFloat(value as Double)
}