package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.function.Parameter
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.Types

/** `name = default`: the type is the default value's type (literals become Int/Double). */
class TParameterDefault(location: Loc, name: String, val def: ITok) : TParameter(location, name) {
    override fun toFunctionParameter(cd: CompileData): Parameter {
        val type = cd.storable(location, def.process(cd, Types.None).first, "parameter '$name'")
        return Parameter(location, testIdentifier(location, name), type, def)
    }

    override fun toString(): String {
        return "TParameterDefault($name, $def)"
    }
}
