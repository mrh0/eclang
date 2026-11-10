package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.function.Parameter
import github.mrh0.ect2.Types

/** `...`: C-style variadic arguments of any type (for C functions such as printf). */
class TParameterVarArgC(location: Loc) : TParameter(location, "unnamed") {
    override fun toFunctionParameter(cd: CompileData) = Parameter(location, name, Types.Any, isCVarArg = true)

    override fun toString(): String {
        return "TParameterVarArgC"
    }
}
