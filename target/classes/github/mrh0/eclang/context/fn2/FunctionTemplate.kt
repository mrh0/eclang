package github.mrh0.eclang.context.fn2

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.fn.EcAmbiguousSignatureError
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeTuple

data class FunctionTemplate(val location: Loc, val namespace: String, val identifier: String) {
    private val node0th: ParameterNode = ParameterNode()
    private val overrides: MutableList<OverrideTemplate> = mutableListOf()
    fun insert(override: OverrideTemplate) {
        overrides.add(override)
        if (override.parameters.isEmpty()) {
            node0th.leaf = override
            return
        }
        val collision = node0th.insert(location, override)
        if (collision != null) {
            throw EcAmbiguousSignatureError(
                location,
                namespace,
                identifier,
                override.parameters.map { it.baseType }.toTypedArray(),
                collision.location
            )
        }
    }

    fun get(location: Loc, arguments: Array<EcType>): OverrideTemplate? {
        if (arguments.isEmpty()) {
            return node0th.leaf
        }
        return node0th.get(location, arguments, 0)
    }

    fun getOverrideCount(): Int = overrides.size

    fun getInternalName(): String = "${namespace}__${identifier}_${overrides.size}"

    fun getOverrides(): List<OverrideTemplate> = overrides

    override fun toString(): String = "$node0th"
}