package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.token.op.Ops
import github.mrh0.eclang.context.ArgumentOrder
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.ect2.ArrayType
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * The element type and converted elements of an array literal. With an expected array type the elements must fit
 * its element type; otherwise the element type is the common type of the elements (`[1, 2.5]` is `Double[]`). The
 * elements run left to right: the caller gives the literal it makes to [order]'s `wrap`.
 */
internal fun arrayElements(cd: CompileData, order: ArgumentOrder, location: Loc, values: List<ITok>, hint: Type, isC: Boolean): Pair<Type, List<IIR>> {
    if (values.isEmpty()) throw EcError(location, "An empty array needs a type, e.g. `val a: Int[] = []`.")
    val expected = (hint as? ArrayType)?.takeIf { it.isC == isC }?.element
    val processed = order.inOrder(location, values) { _, it -> it.process(cd, expected ?: Types.None) }
    val element = expected ?: cd.storable(location,
        processed.map { cd.usable(location, it, "array element").first }.reduce { a, b -> Ops.common(cd, location, "array elements", a, b) },
        "array element")
    return element to processed.mapIndexed { i, it -> cd.expect(location, it, element, "array element ${i + 1}") }
}
