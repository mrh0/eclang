package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRPass
import github.mrh0.ect2.Layout
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** `declare struct, a C struct named struct x`. eclang never emits its definition. */
class TTypeDeclareStruct(location: Loc, val name: String, val names: List<String>, val types: List<ITok>, val externalName: String?, private val widths: List<ITok?> = listOf(), private val isUnion: Boolean = false) : Tok(location), ITypeDeclaration {
    override val parentName: String? get() = null

    override fun declareType(cd: CompileData) {
        checkNewTypeName(cd, location, name)
        cd.types.record(cd.module.name, name, null, externalName, isExternal = true, isCStruct = true, layout = Layout(isUnion = isUnion))
    }

    override fun defineType(cd: CompileData) = defineFields(cd, location, declared(cd, location, name), names, types, widths)

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        declareType(cd)
        defineType(cd)
        return Types.None to IRPass(location, null)
    }

    override fun toString(): String {
        return "TTypeDeclareStruct($name)"
    }
}
