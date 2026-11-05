package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.op.Ops
import github.mrh0.eclang.context.ArgumentOrder
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.ir.data.IRCreateArray
import github.mrh0.eclang.ir.data.IRCreateFixed
import github.mrh0.ect2.FixedArray
import github.mrh0.ect2.Type
import github.mrh0.ect2.Union

class TCreateArray(location: Loc, val values: List<ITok>) : Tok(location) {
    /** Whether the last compiled literal was a `T[N]`, whose elements are in the value and not on the stack (see Lifetimes). */
    var isFixed = false
        private set

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        // `T[N]?` and `T[N] | String` hold a `T[N]` too; among several lengths the literal's own decides.
        val expected = ((hint as? Union)?.members ?: listOf(hint)).filter { Ops.base(it) is FixedArray }
            .let { fixed -> fixed.singleOrNull() ?: fixed.singleOrNull { (Ops.base(it) as FixedArray).size == values.size.toLong() } }
        val fixed = expected?.let { Ops.base(it) as FixedArray }
        isFixed = fixed != null
        val order = ArgumentOrder(cd)
        if (fixed != null) {
            if (values.size.toLong() != fixed.size) throw EcError(location, "A $expected has ${fixed.size} element(s), but this literal has ${values.size}. To start with empty elements, declare it without a value: `var a: $expected`.")
            val irs = order.inOrder(location, values) { _, it -> it.process(cd, fixed.element) }.mapIndexed { i, it -> cd.expect(location, it, fixed.element, "array element ${i + 1}") }
            return expected to order.wrap(location, IRCreateFixed(location, fixed, irs))
        }
        val (element, irs) = arrayElements(cd, order, location, values, hint, false)
        val arrayType = cd.types.array(element)
        val storage = if (irs.isEmpty()) null else cd.literalStorage(location, element, irs.size)
        return arrayType to order.wrap(location, IRCreateArray(location, IRType(location, arrayType), IRType(location, element), irs, storage))
    }

    override fun toString(): String {
        return "TCreateArray($values)"
    }
}
