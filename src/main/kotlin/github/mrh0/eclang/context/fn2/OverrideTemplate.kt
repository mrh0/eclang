package github.mrh0.eclang.context.fn2

import github.mrh0.eclang.ast.Loc

data class OverrideTemplate(val namespace: String, val identifier: String, val location: Loc, val parameters: Array<ParameterTemplate>, val isExternal: Boolean = false) {
    override fun toString(): String = "${parameters.size}"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OverrideTemplate

        if (isExternal != other.isExternal) return false
        if (namespace != other.namespace) return false
        if (identifier != other.identifier) return false
        if (location != other.location) return false
        if (!parameters.contentEquals(other.parameters)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isExternal.hashCode()
        result = 31 * result + namespace.hashCode()
        result = 31 * result + identifier.hashCode()
        result = 31 * result + location.hashCode()
        result = 31 * result + parameters.contentHashCode()
        return result
    }
}