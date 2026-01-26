package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.function.GlobalFunctions
import github.mrh0.eclang.context.signature.CallSignatureInstance
import github.mrh0.eclang.error.fn.EcAmbiguousSignatureError
import github.mrh0.eclang.error.EcUnexpectedTypeError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRNamed
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeAny
import github.mrh0.eclang.types.EcTypeCallSignature

class TNamedFunction (location: Loc, val name: String) : Tok(location) {
    override fun toString(): String {
        return "fn$$name"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val fixedName = testIdentifier(location, name)
        val overrides = GlobalFunctions.getOverridesByName(location, fixedName)
        if (hint !is EcTypeCallSignature) throw EcUnexpectedTypeError(location, hint, EcTypeAny)
        val matching = overrides.getMatching(location, hint)
        if (matching.size > 1) throw EcAmbiguousSignatureError(location, cd.namespace, name, hint.args, matching[0].location)
        return hint to IRNamed(location, fixedName)
    }
}