package github.mrh0.eclang.ast

import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.types.EcTypeAny

interface ITok {
    // Hint is for inferring types
    fun process(cd: CompileData /*, hint: GsType*/): Pair<EcTypeAny, IIR>
}