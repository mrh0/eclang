package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.ArgumentOrder
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.ir.data.IRCreateCArray
import github.mrh0.ect2.Type

class TCreateCArray(location: Loc, val values: List<ITok>) : Tok(location) {
    override fun pointerFlow(): PointerFlow = PointerFlow.From(values)

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val order = ArgumentOrder(cd)
        val (element, irs) = arrayElements(cd, order, location, values, hint, true)
        val storage = if (irs.isEmpty()) null else cd.literalStorage(location, element, irs.size)
        return cd.types.cArray(element) to order.wrap(location, IRCreateCArray(location, IRType(location, element), irs, storage))
    }

    override fun toString(): String {
        return "TCreateCArray($values)"
    }
}
