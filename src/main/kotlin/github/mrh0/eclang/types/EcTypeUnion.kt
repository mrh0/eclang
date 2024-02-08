package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.values.GsBase

class EcTypeUnion(private val types: MutableSet<EcTypeAny>) : EcTypeAny("Gs", "Union") {
    override fun toString() = types.joinToString("|", "(", ")") { it.toString() }
    fun append(type: EcTypeAny) = types.add(type)
    override fun accepts(location: Loc, type: EcTypeAny): Boolean {
        return if(type is EcTypeUnion)
            type.types.all { other -> types.any { it.accepts(location, other) } }
        else
            types.any { it.accepts(location, type) }
    }

    override fun accepts(location: Loc, value: GsBase) = types.any { it.accepts(location, value) }
}