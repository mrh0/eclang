package github.mrh0.eclang.context.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeCallSignature
import github.mrh0.eclang.types.EcTypeUnion

class FunctionOverrides(val name: String, val returnType: EcType) {
    val overrides: MutableList<FunctionOverride> = mutableListOf()
    fun add(fo: FunctionOverride) = overrides.add(fo)
    fun getMatching(location: Loc, types: Array<EcType>) = overrides.filter { it.match(location, types) }
    fun getMatching(location: Loc, signature: EcTypeCallSignature): List<FunctionOverride> {
        return overrides.filter { it.match(location, signature.args) && it.ret.accepts(location, signature.ret) }
    }
    fun getType() = EcTypeUnion(overrides.map { it.getType() }.toHashSet())
    fun getNumberOfOverrides() = overrides.size

    override fun toString() = "FOS($name, $returnType, $overrides)"
}