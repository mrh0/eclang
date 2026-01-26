package github.mrh0.eclang.ast.token.global

import github.mrh0.eclang.Compiler
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.TPass
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.context.state.Variable
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IRGlobalDefine
import github.mrh0.eclang.ir.IRInclude
import github.mrh0.eclang.ir.IRPass
import github.mrh0.eclang.types.EcTypeNone
import github.mrh0.eclang.util.Util
import java.nio.file.Path

class TGlobalUse(location: Loc, val path: String) : Tok(location) {
    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        if (path.endsWith(".ec")) return EcTypeNone to IRPass(location, "use $path")
        else if (path.endsWith(".h") || path.endsWith(".c")) return EcTypeNone to IRInclude(location, path)
        throw EcError(location, "Unknown file extension in use '$path'")
    }

    override fun toString(): String {
        return "TGlobalUse($path)"
    }
}