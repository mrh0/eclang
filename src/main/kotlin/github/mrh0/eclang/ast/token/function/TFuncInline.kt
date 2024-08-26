package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.token.TBlock
import github.mrh0.eclang.ast.token.TStatementReturn
import github.mrh0.eclang.context.function.FunctionParameter
import github.mrh0.eclang.context.function.GlobalFunctions
import github.mrh0.eclang.context.state.Variable
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.ir.function.IRFunctionOverrides
import github.mrh0.eclang.ir.function.IRFunctionOverride
import github.mrh0.eclang.ir.function.IRParameter
import github.mrh0.eclang.ir.function.IRParameters
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeNone

class TFuncInline (location: Loc, val expr: ITok, name: String, params: TParameters, returns: ITok?) : TFunc(location, name, params, returns), IFuncBody {
    override fun toString() = "$name($params, $expr)"

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> = throw NotImplementedError("Should not be implemented")

    override fun getSourceName(): String? {
        return if (name == "main") "main" else null // TODO: Change
    }

    override fun getBody(): TBlock {
        return TBlock(location, listOf(
            TStatementReturn(location, expr)
        ))
    }
}