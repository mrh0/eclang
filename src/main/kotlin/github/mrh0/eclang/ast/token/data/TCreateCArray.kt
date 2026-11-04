package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.ir.data.IRCreateCArray
import github.mrh0.ect2.Type

class TCreateCArray(location: Loc, val values: List<ITok>) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val (element, irs) = arrayElements(cd, location, values, hint, true)
        return cd.types.cArray(element) to IRCreateCArray(location, IRType(location, element), irs)
    }

    override fun toString(): String {
        return "TCreateCArray($values)"
    }
}
