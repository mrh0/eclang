package github.mrh0.eclang.ast.token.function.call

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.function.GlobalFunctions
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.function.call.IRGlobalFunctionCall
import github.mrh0.eclang.ir.function.call.IRArgument
import github.mrh0.eclang.ir.function.call.IRArguments
import github.mrh0.eclang.types.EcType

class TExprCall (location: Loc, val name: String, val args: List<ITok>) : Tok(location) {
    override fun toString(): String {
        return "TCall(${name}, $args)"
    }

    override fun process(cd: CompileData): Pair<EcType, IIR> {
        val overrides = GlobalFunctions.getOverridesByName(location, name)
        val processedArgs = args.map { it.process(cd) }
        val argTypes = processedArgs.map { it.first }.toTypedArray()

        val matching = overrides.getMatching(location, argTypes)[0] // TODO: Select based on return type

        return Pair(
            matching.ret,
            IRGlobalFunctionCall(
                location,
                name,
                matching.id,
                IRArguments(location, processedArgs.map { IRArgument(location, it.second) })
            )
        )
    }
}