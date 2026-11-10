package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.NonValueTok
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRPass
import github.mrh0.ect2.Layout
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** `declare rec, a record defined in C (fields optional)`. eclang never emits its definition. */
class TTypeDeclareRecord(location: Loc, val name: String, val names: List<String>, val types: List<ITok>, val extending: String?, val externalName: String?, private val widths: List<ITok?> = listOf(), private val isUnion: Boolean = false, private val cNames: List<String?> = listOf(), private val isMut: Boolean = false) : NonValueTok(location), ITypeDeclaration {
    override val parentName: String? get() = null

    override fun declareType(cd: CompileData) {
        checkNewTypeName(cd, location, name)
        cd.types.record(cd.module.name, name, null, externalName, isExternal = true, isCStruct = false, layout = Layout(isUnion = isUnion, isMut = isMut))
    }

    override fun defineType(cd: CompileData) = defineFields(cd, location, declared(cd, location, name), names, types, widths, cNames)

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        declareType(cd)
        defineType(cd)
        return Types.None to IRPass(location, null)
    }

    override fun toString(): String {
        return "TTypeDeclareRecord($name)"
    }
}
