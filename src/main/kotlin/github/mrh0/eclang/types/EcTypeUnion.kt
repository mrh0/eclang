package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc

class EcTypeUnion(private val types: MutableSet<EcType>) : EcType("Gs", "Union") {
    override fun toString() = types.joinToString("|", "(", ")") { it.toString() }
    fun append(type: EcType) = types.add(type)
    override fun accepts(location: Loc, type: EcType): Boolean {
        return if(type is EcTypeUnion)
            type.types.all { other -> types.any { it.accepts(location, other) } }
        else
            types.any { it.accepts(location, type) }
    }
}