package github.mrh0.eclang.ast.token.global

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRPass
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeNone

class TGlobalModule(location: Loc, val namespace: String) : Tok(location) {
    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        cd.namespace = namespace // TODO: Make this work with imports etc
        return EcTypeNone to IRPass(location, "module $namespace")
    }

    override fun toString(): String {
        return "TGlobalModule($namespace)"
    }
}