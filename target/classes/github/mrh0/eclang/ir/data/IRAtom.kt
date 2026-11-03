package github.mrh0.eclang.ir.data

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.context.atom.AtomInstance
import github.mrh0.eclang.output.c.CSourceBuilder

class IRAtom(location: Loc, val atom: AtomInstance) : IRValue(location) {
    override fun toString() = atom.toString()

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put(atom.getId())
    }
}