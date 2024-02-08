package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.function.IRFunctionCall
import github.mrh0.eclang.types.EcTypeAny
import github.mrh0.eclang.types.EcTypeCallSignature

class TStatementCall (location: Loc, val name: String, val args: List<ITok>) : Tok(location) {
    override fun toString(): String {
        return "TCall(${name}, $args)"
    }

    override fun process(cd: CompileData): Pair<EcTypeAny, IIR> {
        //val fn = cd.ctx().get(location, name)
        //if(fn.getType() !is GsTypeFunction) throw GsError(location, "Unexpected type")
        val processedArgs = args.map { it.process(cd) }
        val argTypes = processedArgs.map { it.first }.toTypedArray()

        val (varIndex, ivar) = cd.getFunctionVarIndex(location, name, argTypes)
        val varType = ivar.getType() as EcTypeCallSignature
        return Pair(
            varType.ret,
            IRFunctionCall(location,
                name,
                varIndex,
                processedArgs.map { it.second }
            )
        )
    }
}