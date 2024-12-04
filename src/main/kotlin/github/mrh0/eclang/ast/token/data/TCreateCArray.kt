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
import github.mrh0.eclang.ir.data.IRCreateCArray
import github.mrh0.eclang.types.*

class TCreateCArray(location: Loc, private val values: List<ITok>) : Tok(location) {
    override fun toString(): String {
        return "TCreateCArray($values)"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val arrayType: EcTypeCArray = if (hint is EcTypeCArray) hint else {
            val ir0 = values[0].process(cd, EcTypeNone)
            EcTypeCArray(ir0.first)
        }

        val irs = values.map { it.process(cd, arrayType.arg) }
        irs.forEach { if(!arrayType.arg.accepts(location, it.first)) throw EcUnexpectedTypeError(location, arrayType.arg, it.first) }
        return arrayType to IRCreateCArray(location, IRType(location, arrayType.arg), irs.map { it.second })
    }
}