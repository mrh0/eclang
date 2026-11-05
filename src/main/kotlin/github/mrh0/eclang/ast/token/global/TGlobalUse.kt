package github.mrh0.eclang.ast.token.global

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IREmbed
import github.mrh0.eclang.ir.IRInclude
import github.mrh0.eclang.ir.IRPass
import github.mrh0.eclang.util.Util
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

class TGlobalUse(location: Loc, val path: String) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        if (path.startsWith(LIBRARY)) return link(cd, path.removePrefix(LIBRARY))
        if (path.endsWith(".ec")) return Types.None to IRPass(location, "use $path")
        if (path.startsWith("core:")) {
            if (path.endsWith(".c")) return Types.None to IREmbed(location, path, Util.getStream(location, path).bufferedReader().use { it.readText() })
            throw EcError(location, "`core:` only holds eclang files and bundled C sources; include a C header by its own name, e.g. `use \"stdio.h\"`.")
        }
        else if (path.endsWith(".h") || path.endsWith(".c")) {
            cd.includeHeader(path)
            return Types.None to IRInclude(location, path)
        }
        throw EcError(location, "Unknown file extension in use '$path'")
    }

    /** `use "lib:z"`: `run` and `lib` pass `-lz` to the C compiler. The C file says so for those who build it by hand. */
    private fun link(cd: CompileData, name: String): Pair<Type, IIR> {
        if (!Regex("[A-Za-z0-9_][A-Za-z0-9_+.-]*").matches(name))
            throw EcError(location, "`use \"$LIBRARY$name\"` needs the name of a C library, as `-l` takes it: `use \"${LIBRARY}z\"` links libz. Other linker options can't go here.")
        cd.linkedLibraries += name
        return Types.None to IRPass(location, "Link with -l$name")
    }

    override fun toString(): String {
        return "TGlobalUse($path)"
    }

    private companion object {
        const val LIBRARY = "lib:"
    }
}
