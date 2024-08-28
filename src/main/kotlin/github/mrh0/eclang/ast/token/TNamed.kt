package github.mrh0.eclang.ast.token

import github.mrh0.eclang.Util.testIdentifier
import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.error.EcProtectedPrefixError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRNamed
import github.mrh0.eclang.types.EcType

class TNamed (location: Loc, val name: String) : Tok(location) {
    override fun toString(): String {
        return "$${name}"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        testIdentifier(location, name)
        val v = cd.getVar(location, name)
        return v.getType() to v.toIR(location, cd, hint)
    }
}