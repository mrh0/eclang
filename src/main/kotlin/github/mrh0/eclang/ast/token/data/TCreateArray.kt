package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.array.ArrayInstance
import github.mrh0.eclang.error.EcUnableToInferTypeError
import github.mrh0.eclang.error.EcUnexpectedTypeError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.ir.data.IRCreateArray
import github.mrh0.eclang.types.*

class TCreateArray(location: Loc, private val values: List<ITok>) : Tok(location) {
    override fun toString(): String {
        return "TCreateArray($values)"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val arrayType: EcTypeArray = if (hint is EcTypeArray) hint else {
            val ir0 = values[0].process(cd, EcTypeNone)
            EcTypeArray(ir0.first)
        }

        val irs = values.map { it.process(cd, arrayType.arg) }
        irs.forEach { if(!arrayType.arg.accepts(location, it.first)) throw EcUnexpectedTypeError(location, arrayType.arg, it.first) }
        ArrayInstance.get(arrayType.arg)
        return arrayType to IRCreateArray(location, IRType(location, arrayType), IRType(location, arrayType.arg), irs.map { it.second })
    }
}