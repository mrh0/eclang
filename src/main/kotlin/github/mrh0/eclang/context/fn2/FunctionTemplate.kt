package github.mrh0.eclang.context.fn2

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeTuple

data class FunctionTemplate(val namespace: String, val identifier: String, val location: Loc) {
    private val node0th: ParameterNode = ParameterNode()
    fun insert(override: OverrideTemplate) {
        if (override.parameters.isEmpty()) {
            node0th.leaf = override
            return
        }
        node0th.insert(location, override, 0)
    }

    fun get(location: Loc, arguments: Array<EcType>): OverrideTemplate? {
        if (arguments.isEmpty()) {
            return node0th.leaf
        }
        return node0th.get(location, arguments, 0)
    }

    override fun toString(): String = "$node0th"
}