package github.mrh0.eclang.ast.token.data.record

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.error.EcAssignTypeError
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.error.EcUnableToInferTypeError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRBool
import github.mrh0.eclang.ir.data.record.IRCreateRecord
import github.mrh0.eclang.ir.data.record.IRHere
import github.mrh0.eclang.types.BuiltInTypes
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeBool
import github.mrh0.eclang.types.EcTypeRecord

class THere(location: Loc) : Tok(location) {
    override fun toString(): String {
        return "THere"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        return hint to IRHere(location)
    }
}