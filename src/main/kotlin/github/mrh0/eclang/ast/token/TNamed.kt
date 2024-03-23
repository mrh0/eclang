package github.mrh0.eclang.ast.token

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRNamed
import github.mrh0.eclang.types.EcType

class TNamed (location: Loc, val name: String) : Tok(location) {
    override fun toString(): String {
        return "$${name}"
    }

    override fun process(cd: CompileData): Pair<EcType, IIR> {
        return Pair(cd.getVar(location, name).getType(), IRNamed(location, name, cd.ctx().getIndex(location, name)))
    }
}