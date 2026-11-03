package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRPass
import github.mrh0.eclang.types.BuiltInTypes
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeCStruct

class TTypeDeclareStruct(location: Loc, val name: String, val names: List<String>, val types: List<ITok>, val externalName: String?) : Tok(location) {
    override fun toString(): String {
        return "TDefDeclareRecord(${names.mapIndexed { index, it -> it to types[index] }})"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val pairs = names.mapIndexed { index, it -> it to types[index].process(cd, hint) }
        val type = EcTypeCStruct(name, pairs.map { it.first to it.second.first }, externalName)
        BuiltInTypes.defineType(location, cd.namespace, name, type)
        return type to IRPass(location, "declare struct $externalName as $name")
    }
}