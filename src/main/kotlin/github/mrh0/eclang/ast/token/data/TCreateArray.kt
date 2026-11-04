package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.op.Ops
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.ir.data.IRCreateArray
import github.mrh0.eclang.ir.data.IRCreateFixed
import github.mrh0.ect2.FixedArray
import github.mrh0.ect2.Type

class TCreateArray(location: Loc, val values: List<ITok>) : Tok(location) {
    /** Whether the last compiled literal was a `T[N]`, whose elements are in the value and not on the stack (see Lifetimes). */
    var isFixed = false
        private set

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val expected = cd.types.nonNull(hint).let { if (it is FixedArray) it else Ops.base(it).takeIf { base -> base is FixedArray }?.let { _ -> it } }
        val fixed = expected?.let { Ops.base(it) as FixedArray }
        isFixed = fixed != null
        if (fixed != null) {
            if (values.size.toLong() != fixed.size) throw EcError(location, "A $expected has ${fixed.size} element(s), but this literal has ${values.size}. To start with empty elements, declare it without a value: `var a: $expected`.")
            val irs = values.mapIndexed { i, it -> cd.expect(location, it.process(cd, fixed.element), fixed.element, "array element ${i + 1}") }
            return expected to IRCreateFixed(location, fixed, irs)
        }
        val (element, irs) = arrayElements(cd, location, values, hint, false)
        val arrayType = cd.types.array(element)
        return arrayType to IRCreateArray(location, IRType(location, arrayType), IRType(location, element), irs)
    }

    override fun toString(): String {
        return "TCreateArray($values)"
    }
}
