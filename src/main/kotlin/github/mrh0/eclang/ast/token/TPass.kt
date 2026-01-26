package github.mrh0.eclang.ast.token

import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRPass
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeNone

class TPass (location: Loc) : Tok(location) {
    override fun toString(): String {
        return "Pass"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        return EcTypeNone to IRPass(location, null)
    }
}