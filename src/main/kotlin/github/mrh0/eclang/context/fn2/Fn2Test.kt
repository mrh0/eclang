package github.mrh0.eclang.context.fn2

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.EcTypeAny
import github.mrh0.eclang.types.EcTypeBool
import github.mrh0.eclang.types.EcTypeString
import github.mrh0.eclang.types.EcTypeTuple
import github.mrh0.eclang.types.numbers.signed.EcTypeInt

fun main(args: Array<String>) {
    val scope = ModuleScope()

    val overrideA1 = OverrideTemplate("test", "a", Loc.IDENTITY, arrayOf())
    val overrideB1 = OverrideTemplate("test", "b", Loc.IDENTITY, arrayOf())

    val overrideA2 = OverrideTemplate("test", "a", Loc.IDENTITY, arrayOf(ParameterTemplate("oneA2", EcTypeAny)))

    val overrideA3 = OverrideTemplate("test", "a", Loc.IDENTITY, arrayOf(
        ParameterTemplate("one", EcTypeString)))

    val overrideA4 = OverrideTemplate("test", "a", Loc.IDENTITY, arrayOf(
        ParameterTemplate("one", EcTypeInt),
        ParameterTemplate("two", EcTypeString)))

    scope.addOverride(overrideA1)
    scope.addOverride(overrideB1)
    scope.addOverride(overrideA2)
    scope.addOverride(overrideA3)
    scope.addOverride(overrideA4)

    println(scope.getOverride(Loc.IDENTITY, "a", arrayOf(EcTypeBool)))

    println(scope)
}