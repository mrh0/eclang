package github.mrh0.eclang.ast.token.op

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.error.EcNotReferenceTypeError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRAddressOf
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypePointer
import github.mrh0.eclang.types.numbers.signed.EcTypeInt
import github.mrh0.eclang.util.Util

class TAddressOf(location: Loc, val name: String) : Tok(location) {
    override fun toString(): String {
        return "TAddressOf($name)"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val fixedName = Util.testIdentifier(location, name)
        val v = cd.getVar(location, fixedName)
        val type = EcTypePointer(v.getType())
        return type to IRAddressOf(location, v.toIR(location, cd, hint))
    }
}
