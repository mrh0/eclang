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
    fun get() = args
    override fun process(cd: CompileData): Pair<EcType, IIR> =
        EcTypeNone to IRParameters(location, args.map { IRParameter(it.location, it.name, it.type.process(cd).first) })

    override fun toString() = "$args"
}