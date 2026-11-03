package github.mrh0.eclang.context.fn2

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Module
import github.mrh0.eclang.types.EcTypeAny
import github.mrh0.eclang.types.EcTypeBool
import github.mrh0.eclang.types.EcTypeString
import github.mrh0.eclang.types.numbers.signed.EcTypeInt

fun main(args: Array<String>) {
    val scope = Module.get("test")

    val overrideA1 = OverrideTemplate.of(Loc.IDENTITY, "a", EcTypeAny, arrayOf())
    val overrideB1 = OverrideTemplate.of(Loc.IDENTITY, "b", EcTypeAny, arrayOf())

    val overrideA2 = OverrideTemplate.of(Loc.IDENTITY, "a", EcTypeAny, arrayOf(ParameterTemplate("oneA2", EcTypeAny)))

    val overrideA3 = OverrideTemplate.of(Loc.IDENTITY, "a", EcTypeAny, arrayOf(
        ParameterTemplate("one", EcTypeString)))

    val overrideA4 = OverrideTemplate.of(Loc.IDENTITY, "a", EcTypeAny, arrayOf(
        ParameterTemplate("one", EcTypeInt),
        ParameterTemplate("two", EcTypeString)))

    scope.addOverride("a", overrideA1)
    scope.addOverride("b", overrideB1)
    scope.addOverride("a", overrideA2)
    scope.addOverride("a", overrideA3)
    scope.addOverride("a", overrideA4)

    println(scope.getOverride(Loc.IDENTITY, "a", arrayOf(EcTypeBool)))

    println(scope)
}