package github.mrh0.eclang.context.fn2

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.util.Util.testIdentifier

data class OverrideTemplate private constructor(val location: Loc, val sourceName: String, val returnType: EcType, val parameters: Array<ParameterTemplate>) {
    companion object {
        fun of(location: Loc, sourceName: String, returnType: EcType, parameters: Array<ParameterTemplate>) =
            OverrideTemplate(location, testIdentifier(location, sourceName), returnType, parameters)
    }

    override fun toString(): String = "${parameters.size}"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OverrideTemplate

        return sourceName == other.sourceName
    }

    override fun hashCode(): Int {
        return sourceName.hashCode()
    }
}