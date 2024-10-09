package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.error.EcAssignTypeError
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.error.EcUnableToInferTypeError
import github.mrh0.eclang.error.EcUnexpectedTypeError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.ir.data.IRBool
import github.mrh0.eclang.ir.data.IRCreateArray
import github.mrh0.eclang.ir.data.record.IRCreateRecord
import github.mrh0.eclang.types.*

class TCreateArray(location: Loc, private val values: List<ITok>) : Tok(location) {
    override fun toString(): String {
        return "TCreateArray($values)"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        if (hint !is EcTypeArray) throw EcUnableToInferTypeError(location)
        val irs = values.map { it.process(cd, hint.arg) }
        irs.forEach { if(!hint.arg.accepts(location, it.first)) throw EcUnexpectedTypeError(location, hint.arg, it.first) }
        return hint to IRCreateArray(location, IRType(location, hint), irs.map { it.second })
    }
}