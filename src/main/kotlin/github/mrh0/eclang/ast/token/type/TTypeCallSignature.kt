package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRPass
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.ir.data.record.IRRecord
import github.mrh0.eclang.types.*

class TTypeCallSignature(location: Loc, val argTypes: List<ITok>, val returnType: ITok?) : Tok(location) {
    override fun toString(): String {
        return "TTypeCallSignature(${argTypes.joinToString { it.toString() }}, => ${returnType})"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val types = argTypes.map { it.process(cd, hint).first }
        val retType = returnType?.process(cd, hint)?.first ?: EcTypeNone
        val type = EcTypeCallSignature(types.toTypedArray(), retType)
        return type to IRType(location, type)
    }
}