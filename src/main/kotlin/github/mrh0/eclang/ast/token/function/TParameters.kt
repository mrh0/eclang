package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.function.IRParameter
import github.mrh0.eclang.ir.function.IRParameters
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeNone

class TParameters(location: Loc, val args: MutableList<TParameter>) : Tok(location) {
    fun get() = args.map { it }
    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> = throw NotImplementedError()
        // EcTypeNone to IRParameters(location, args.map { it.process(cd).second as IRParameter })

    fun toFunctionParameters(cd: CompileData) {

    }

    override fun toString() = "$args"
}