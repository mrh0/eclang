package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRNamed
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.FunctionType
import github.mrh0.ect2.Type
import github.mrh0.ect2.Union

/** `fn name`: a function as a value. The expected function type picks the overload. It captures nothing (see [TLambda]). */
class TNamedFunction (location: Loc, val name: String, private val qualifier: String? = null) : Tok(location) {
    override fun pointerFlow(): PointerFlow = PointerFlow.Nothing

    override fun toString(): String {
        return "fn$$name"
    }

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val module = qualifier?.let { cd.findModule(it) ?: throw EcError(location, "There is no module called '$it'.") }
        val signature = hint as? FunctionType ?: (hint as? Union)?.members?.filterIsInstance<FunctionType>()?.singleOrNull()
            ?: cd.functions.onlyOverload(testIdentifier(location, name), module)?.let { cd.functions.typeOf(it) }
            ?: throw EcError(location, "`$name` has several overloads, so it's unclear which one is meant. Give the variable a function type, e.g. `val f: (Int) => Int = fn $name`.")
        val overload = cd.functions.reference(location, testIdentifier(location, name), signature.let { if (it.isC) cd.types.function(it.params, it.returns, it.throws) else it }, module)
        // What it keeps in its arguments is checked where values of its type are called; elsewhere, it can't be (see Escapes).
        overload.body?.let { cd.escapes.functionValue(location, name, it, overload.params.map { p -> p.name }, signature, cd.isUnsafe) }
        // Where C expects a function pointer it is the C function itself; as an eclang function value, a closure of it.
        return signature to if (signature.isC) IRNamed(location, overload.cName) else cd.functions.closureOf(location, overload, signature)
    }
}
