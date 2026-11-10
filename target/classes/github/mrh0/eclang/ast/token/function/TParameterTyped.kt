package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.function.Parameter
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.Types

/** `name: T` */
class TParameterTyped(location: Loc, name: String, val type: ITok) : TParameter(location, name) {
    override fun toFunctionParameter(cd: CompileData) =
        Parameter(location, testIdentifier(location, name), type.process(cd, Types.None).first)

    override fun toString(): String {
        return "TParameterTyped($name, $type)"
    }
}
