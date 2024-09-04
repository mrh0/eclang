package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.token.TBlock
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.types.EcType

class TFuncBlock (location: Loc, val block: TBlock, name: String, params: TParameters, returns: ITok?) : TFunc(location, name, params, returns), IFuncBody {
    override fun toString() = "$name($params, $block)"

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> = throw NotImplementedError("Should not be implemented")

    override fun getSourceName(): String? {
        return null // TODO: Change
    }

    override fun getBody(): TBlock = block
}