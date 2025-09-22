package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.atom.AtomInstance
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRAtom
import github.mrh0.eclang.types.EcTypeAtomInstance
import github.mrh0.eclang.types.EcType

class TAtom(location: Loc, private val value: String) : Tok(location) {
    override fun toString(): String {
        return "TAtom(${value})"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        testIdentifier(location, value)
        val instance = AtomInstance.get(value)
        return EcTypeAtomInstance.of(instance) to IRAtom(location, instance)
    }
}