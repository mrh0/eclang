package github.mrh0.eclang.ast

import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.ect2.Type

interface ITok {
    /**
     * Type-checks this token and lowers it to IR, returning its type and IR. [hint] is the type the
     * surroundings expect (Types.None when nothing is expected); it lets `{...}`, `[...]` and number literals
     * pick their type.
     */
    fun process(cd: CompileData, hint: Type): Pair<Type, IIR>

    /**
     * What the value of this token may point into, for `Lifetimes` (dangling pointers and freed pools). It has no
     * default on purpose: a new kind of expression must say what it passes through, or the checks would take it for a
     * value that holds no pointer. Called after [process], so it may use what [process] found (a call's `aliased`).
     */
    fun pointerFlow(): PointerFlow
}

/** See [ITok.pointerFlow]. */
sealed interface PointerFlow {
    /** A statement, declaration, type or parameter: it has no value. */
    data object NotAValue : PointerFlow

    /**
     * A value that holds no pointer into another value's memory: a number, a Bool, a literal in static memory, a
     * function. Also a value that `Lifetimes` follows by its own rules: a variable, `addrof`, a pipe and its `it`,
     * and the copy `"${...}"` makes in the current pool. Such a token must have a case in `Lifetimes.value`.
     */
    data object Nothing : PointerFlow

    /** A value that may point into what [values] point into: the parts of a literal, a call's arguments, what a read reads from. */
    class From(val values: List<ITok>) : PointerFlow {
        constructor(vararg values: ITok?) : this(values.filterNotNull())
    }
}
