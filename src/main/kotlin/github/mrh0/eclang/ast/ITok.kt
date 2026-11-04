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
}
