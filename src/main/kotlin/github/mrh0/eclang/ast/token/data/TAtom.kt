package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRAtom
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.Type

class TAtom(location: Loc, val value: String) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        testIdentifier(location, value)
        val instance = cd.atoms.get(value)
        return cd.types.atom(instance.label) to IRAtom(location, instance)
    }

    override fun toString(): String {
        return "TAtom($value)"
    }
}
