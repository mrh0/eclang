package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRPass
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * `inter Name as` + fields: any record with these fields (same names and types) fits. Used as a parameter type,
 * each record passed in gets its own copy of the function.
 */
class TInterface(location: Loc, val name: String, val names: List<String>, val types: List<ITok>) : Tok(location), ITypeDeclaration {
    override val parentName: String? get() = null

    override fun declareType(cd: CompileData) {
        checkNewTypeName(cd, location, name)
        cd.types.interfaceType(cd.module.name, name)
    }

    override fun defineType(cd: CompileData) = defineFields(cd, location, declared(cd, location, name), names, types)

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        declareType(cd)
        defineType(cd)
        return Types.None to IRPass(location, null)
    }

    override fun toString(): String {
        return "TInterface($name)"
    }
}
