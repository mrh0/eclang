package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.function.Parameter
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.Types

/** `name: T = default`: can be left out; the default may use earlier parameters. */
class TParameterTypedDefault(location: Loc, name: String, val type: ITok, val def: ITok) : TParameter(location, name) {
    override fun toFunctionParameter(cd: CompileData) =
        Parameter(location, testIdentifier(location, name), type.process(cd, Types.None).first, def)

    override fun toString(): String {
        return "TParameterTypedDefault($name, $type, $def)"
    }
}
