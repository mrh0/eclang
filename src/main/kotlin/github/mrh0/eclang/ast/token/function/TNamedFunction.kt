package github.mrh0.eclang.ast.token.function

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

/** `fn name`: a function as a value. The expected function type picks the overload. */
class TNamedFunction (location: Loc, val name: String, private val qualifier: String? = null) : Tok(location) {
    override fun toString(): String {
        return "fn$$name"
    }

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val module = qualifier?.let { cd.findModule(it) ?: throw EcError(location, "There is no module called '$it'.") }
        val signature = hint as? FunctionType ?: (hint as? Union)?.members?.filterIsInstance<FunctionType>()?.singleOrNull()
            ?: cd.functions.onlyOverload(testIdentifier(location, name), module)?.let { cd.functions.typeOf(it) }
            ?: throw EcError(location, "`$name` has several overloads, so it's unclear which one is meant. Give the variable a function type, e.g. `val f: (Int) => Int = fn $name`.")
        val overload = cd.functions.reference(location, testIdentifier(location, name), signature, module)
        return signature to IRNamed(location, overload.cName)
    }
}
