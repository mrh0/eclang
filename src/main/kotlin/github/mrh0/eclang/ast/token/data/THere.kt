package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRValue
import github.mrh0.eclang.types.EcTypeAny
import github.mrh0.eclang.types.EcTypeString
import github.mrh0.eclang.types.EcTypeTuple
import github.mrh0.eclang.types.numbers.EcTypeInt

class THere(location: Loc) : Tok(location) {
    override fun toString(): String {
        return "THere"
    }

    override fun process(cd: CompileData): Pair<EcTypeAny, IIR> {
        return EcTypeTuple(arrayOf(EcTypeInt, EcTypeInt, EcTypeString)) to IRValue(location, location.asTuple())
    }
}