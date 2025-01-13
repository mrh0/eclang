package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.types.*

class TTypeThrows(location: Loc, val returnType: ITok, val throwsType: ITok) : Tok(location) {
    override fun toString() = "($returnType throw $throwsType)"

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val returnTypeRes = returnType.process(cd, hint)
        val throwableTypeRes = throwsType.process(cd, hint)
        val catchable = EcTypeResult(returnTypeRes.first, throwableTypeRes.first)
        return catchable to IRType(location, catchable)
    }
}