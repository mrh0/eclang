package github.mrh0.eclang.context.fn2

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcAmbiguousSignatureError
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeTuple

data class ParameterNode(val parameter: ParameterTemplate? = null, var leaf: OverrideTemplate? = null, val nodes: MutableList<ParameterNode> = mutableListOf()) {
    fun insert(location: Loc, override: OverrideTemplate, index: Int) {
        val existing = nodes.find { it.parameter!!.baseType.accepts(location, override.parameters[index].baseType)  }
        val nextNode = existing ?: ParameterNode(override.parameters[index])
        if (existing == null) nodes.add(nextNode)
        if (index == override.parameters.size-1) {
            if (nextNode.leaf != null) {
                throw EcAmbiguousSignatureError(
                    location,
                    override.identifier,
                    override.parameters.map { it.baseType }.toTypedArray(),
                    listOf(nextNode.leaf!!.location)
                )
            }
            nextNode.leaf = override
        } else {
            nextNode.insert(location, override, index + 1)
        }
    }

    fun get(location: Loc, arguments: Array<EcType>, index: Int): OverrideTemplate? {
        val existing = nodes.find { it.parameter!!.baseType.accepts(location, arguments[index])  }
        if (existing == null) return null
        if (index == arguments.size-1) return existing.leaf
        if (existing.parameter!!.isVarArg) return existing.leaf
        return existing.get(location, arguments, index + 1)
    }

    override fun toString(): String = "($leaf:$nodes)"
}