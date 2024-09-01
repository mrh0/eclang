package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.atom.AtomInstance
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.types.BuiltInTypes
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeAtomInstance

class TTypeAtom(location: Loc, val type: String) : Tok(location) {
    override fun toString() = "#$type"

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val atom = AtomInstance.get(type.drop(1))
        val atomType = EcTypeAtomInstance(atom)
        return Pair(atomType, IRType(location, atomType))
    }
}