package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRPass
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.ir.data.record.IRRecord
import github.mrh0.eclang.types.BuiltInTypes
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeRecord

class TTypeDeclareRecord(location: Loc, val name: String, val names: List<String>, val types: List<ITok>, val extending: String?, val externalName: String?) : Tok(location) {
    override fun toString(): String {
        return "TDeclareRecord(${names.mapIndexed { index, it -> it to types[index] }})"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val pairs = names.mapIndexed { index, it -> it to types[index].process(cd, hint) }
        val type = EcTypeRecord(name, pairs.map { it.first to it.second.first }, extending, externalName)
        BuiltInTypes.defineType(location, cd.namespace, name, type)
        return type to IRPass(location, "declare rec $externalName as $name")
    }
}