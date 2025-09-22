package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.types.BuiltInTypes
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeEnum
import github.mrh0.eclang.types.EcTypeUnion

class TTypeEnum(location: Loc, val primitives: List<ITok>) : Tok(location) {
    override fun toString() = "#Enum($primitives)"

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val set = mutableSetOf<EcType>()
        val processedTypes = primitives.map { it.process(cd, hint).first }
        processedTypes.forEach {
            if (it is EcTypeEnum) set.addAll(it.expand()) else set.add(it)
        }
        val enum = EcTypeEnum.of(set)
        return enum to IRType(location, enum)
    }
}