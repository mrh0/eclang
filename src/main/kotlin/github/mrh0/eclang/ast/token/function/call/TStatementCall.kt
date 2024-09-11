package github.mrh0.eclang.ast.token.function.call

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.function.GlobalFunctions
import github.mrh0.eclang.error.EcAmbiguousSignatureError
import github.mrh0.eclang.error.EcNoMatchingCallSignatureError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRStatement
import github.mrh0.eclang.ir.function.call.IRArgument
import github.mrh0.eclang.ir.function.call.IRArguments
import github.mrh0.eclang.ir.function.call.IRGlobalFunctionCall
import github.mrh0.eclang.types.EcType

class TStatementCall (location: Loc, val name: String, val args: List<ITok>) : Tok(location) {
    override fun toString(): String {
        return "TCall(${name}, $args)"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val overrides = GlobalFunctions.getOverridesByName(location, name)
        val processedArgs = args.map { it.process(cd, hint) }
        val argTypes = processedArgs.map { it.first }.toTypedArray()

        val matching = overrides.getMatching(location, argTypes)
        if (matching.isEmpty()) throw EcNoMatchingCallSignatureError(location, name, argTypes)
        if (matching.size > 1) throw EcAmbiguousSignatureError(location, name, argTypes, matching.map { it.location })
        val first = matching[0]

        first.setCalled()

        return Pair(
            first.ret,
            IRStatement(
                location,
                IRGlobalFunctionCall(
                    location,
                    name,
                    first.id,
                    IRArguments(location, processedArgs.map { IRArgument(location, it.second) })
                )
            )
        )
    }
}