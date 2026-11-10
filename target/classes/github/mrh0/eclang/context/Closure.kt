package github.mrh0.eclang.context

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.function.Overload
import github.mrh0.eclang.context.state.IVar
import github.mrh0.eclang.ir.IIR
import github.mrh0.ect2.Type

/**
 * A lambda while its body is compiled ([CompileData.inClosure]): what it captures of the function around it, and what its
 * `try`s can fail with.
 *
 * A lambda uses the locals around it by name. A `val` or parameter can't change, so the closure keeps a copy (with the type
 * a check before the lambda narrowed it to); a `var` is shared, so the closure keeps a pointer to it, and Lifetimes treats the
 * lambda like `addrof` of it (it can't outlive the variable). The closure is allocated in the current pool when it captures
 * anything; otherwise it is a static C object (see `TLambda`).
 *
 * [outer], [outerFunction] and [outerGenerics] are the function around the lambda (where captured values are read), [parent]
 * the lambda around this one, if any. [inC]: the lambda becomes a plain C function pointer, which can't capture anything.
 * [changes]: the names the body may change (`mut x`, `addrof x`, `x.f()`), from the Visitor.
 */
class Closure(
    val location: Loc, val outer: ContextBuilder, val outerFunction: Overload?, val outerGenerics: Map<String, Type>?, val parent: Closure?,
    val inC: Boolean, val changes: Set<String>, val throws: Type?,
    /** The names of the lambda's parameters. */
    val params: List<String> = listOf(),
) {
    /**
     * What [Escapes] follows the lambda's stores in terms of: its [params], and for each capture `x`, `^x` (the value of a `val`, or
     * the place of a `var`, which `addrof x` points at) and, for a `var`, `^x*` (its value). [Lifetimes.noteLambda] checks them where
     * the lambda is written, as if it were called there; what it keeps of its [params] is an error (see [Escapes.check]).
     */
    val escapeParams: MutableSet<String> = LinkedHashSet(params)

    /**
     * A variable the lambda uses: its [field] in the closure struct, of [type] (a pointer to it when [byReference]), the
     * value it starts with ([init], compiled outside the lambda), [token] for Lifetimes (`x`, or `addrof x` for a `var`),
     * and [inner], what the name means inside the lambda.
     */
    class Capture(val name: String, val field: String, val type: Type, val byReference: Boolean, val init: IIR, val token: ITok, val inner: IVar)

    val captures = LinkedHashMap<String, Capture>()

    /** The errors the `try`s in the body can pass on, when the lambda's `throws` isn't given ([throws] is null). */
    val thrown = LinkedHashSet<Type>()

    /** The result type the lambda returns once known, for the `try`s in it, which are compiled before its body's type is. */
    var result: github.mrh0.ect2.Result? = null
}
