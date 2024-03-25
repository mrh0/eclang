package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.types.BuiltInTypes
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeUnion

class TTypeUnion(location: Loc, val types: List<ITok>) : Tok(location) {
    override fun toString() = "#Union($types)"

    override fun process(cd: CompileData): Pair<EcType, IIR> {
        val set = mutableSetOf<EcType>()
        val processedTypes = types.map { it.process(cd).first }
        processedTypes.forEach {
            if (it is EcTypeUnion) set.addAll(it.expand()) else set.add(it)
        }
        val union = EcTypeUnion(set)
        return union to IRType(location, union)
    }
}