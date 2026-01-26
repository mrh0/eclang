package github.mrh0.eclang.ast.token.function.call

import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.statement.TStatement
import github.mrh0.eclang.context.function.GlobalFunctions
import github.mrh0.eclang.error.fn.EcAmbiguousSignatureError
import github.mrh0.eclang.error.fn.EcNoMatchingCallSignatureError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRStatement
import github.mrh0.eclang.ir.function.call.IRArgument
import github.mrh0.eclang.ir.function.call.IRArguments
import github.mrh0.eclang.ir.function.call.IRGlobalFunctionCall
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeNone

class TStatementCall (location: Loc, name: String, args: List<ITok>) : TExprCall(location, name, args) {
    override fun toString(): String {
        return "TStatementCall(${name}, $args)"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        return EcTypeNone to IRStatement(location, super.process(cd, hint).second)
    }
}