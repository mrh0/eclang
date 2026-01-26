package github.mrh0.eclang.ast.token.global

import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRPass
import github.mrh0.eclang.types.BuiltInTypes
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeNone

class TGlobalTypeDefine(location: Loc, val name: String, val type: ITok) : Tok(location) {
    override fun toString(): String {
        return "TTypeDefine($name, $type)"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        BuiltInTypes.defineType(location, cd.namespace, name, type.process(cd, hint).first)
        return EcTypeNone to IRPass(location, null)
    }
}