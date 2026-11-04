package github.mrh0.eclang.ast.token.function.call

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRStatement
import github.mrh0.ect2.Result
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** A call used as a statement. Its value is dropped, but an error it can return may not be. */
class TStatementCall (location: Loc, name: String, args: List<ITok>, isMethod: Boolean = false) : TExprCall(location, name, args, isMethod) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val (type, ir) = super.process(cd, hint)
        if (type is Result) throw EcError(location, "The error from $name(...) is ignored: ${cd.types.explain(type, cd.types.concrete(type.value))}.")
        return Types.None to IRStatement(location, ir)
    }
}
