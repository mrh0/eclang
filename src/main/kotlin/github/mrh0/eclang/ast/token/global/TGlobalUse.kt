package github.mrh0.eclang.ast.token.global

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRInclude
import github.mrh0.eclang.ir.IRPass
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

class TGlobalUse(location: Loc, val path: String) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        if (path.endsWith(".ec")) return Types.None to IRPass(location, "use $path")
        if (path.startsWith("core:")) throw EcError(location, "`core:` only holds eclang files; include a C header by its own name, e.g. `use \"stdio.h\"`.")
        else if (path.endsWith(".h") || path.endsWith(".c")) return Types.None to IRInclude(location, path)
        throw EcError(location, "Unknown file extension in use '$path'")
    }

    override fun toString(): String {
        return "TGlobalUse($path)"
    }
}
