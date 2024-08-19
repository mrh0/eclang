package github.mrh0.eclang.ast

import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.types.EcType

interface ITok {
    // Hint is for inferring types
    fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR>
}