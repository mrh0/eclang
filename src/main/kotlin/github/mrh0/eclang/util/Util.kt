package github.mrh0.eclang.util

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.error.EcProtectedPrefixError
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.file.Path
import java.nio.file.Paths

object Util {
    fun getCStringContent(token: String?) = token?.substring(1, token.length-2)
    fun getStringContent(token: String?) = token?.substring(1, token.length-1)

    fun testIdentifier(location: Loc, id: String): String {
        if (id.startsWith("__ec_")) throw EcProtectedPrefixError(location)
        return when (id) {
            "auto", "else", "long", "switch", "break", "enum", "register", "typedef",
            "case", "extern", "return", "union", "char", "float", "short", "unsigned",
            "const", "for", "signed", "void", "continue", "goto", "sizeof", "volatile",
            "default", "if", "static", "while", "do", "int", "struct", "_Packed", "double",
            "inline", "restrict", "bool", "true", "false", "nullptr", "alignas", "alignof", "constexpr",
            "static_assert", "thread_local", "typeof", "typeof_unqual", "asm"
            -> "_$id"
            else -> id
        }
    }

    /** Names the C headers every generated file includes declare at file scope (see tools/gen-c-reserved.py). */
    private val cReserved: Set<String> by lazy {
        Util::class.java.classLoader.getResourceAsStream("c-reserved.txt")!!.bufferedReader().readLines().toSet()
    }

    /** True when [cName] can't be used for a definition in the generated C, because C or its headers already use it. */
    fun isCReserved(cName: String) = cName in cReserved || cName.startsWith("__") || Regex("_[A-Z].*").matches(cName) ||
        cName.startsWith("apr_") || cName.startsWith("APR_")

    @Deprecated("Use getStream")
    fun getPath(location: Loc, relativePath: String): Path {
        if (relativePath.contains(":")) {
            val split = relativePath.split(":")
            if (split.size > 2) throw EcError(location, "Malformed path '$relativePath'")
            if (split[0] == "core") {
                val path = split[1]
                return Path.of(Util::class.java.classLoader.getResource(path)?.toURI() ?: throw EcError(location, "File not found at path '$relativePath'"))
            }
            else throw EcError(location, "Unknown path prefix '${split[0]}' at '$relativePath'")
        }
        if (Path.of(relativePath).isAbsolute) return Path.of(relativePath)
        val workingDirectory = System.getProperty("user.dir")
        val path = Paths.get(workingDirectory, relativePath)

        return path.toAbsolutePath()
    }

    /**
     * Where `use "path"` (written at [location]) points: "core:..." paths stay as they are; other paths are
     * looked up next to the file that has the `use` (inside core: for a bundled file), then in the working
     * directory. Returns an absolute path, or a core: path.
     */
    fun resolveUse(location: Loc, path: String): String {
        if (path.contains(":") || Path.of(path).isAbsolute) return path
        // Inside a bundled file, a plain path is another bundled file: `use "pools.ec"` in core:apr/env.ec.
        location.file?.path?.takeIf { it.startsWith("core:") }?.let { return Path.of(it).resolveSibling(path).normalize().toString() }
        val besideFile = location.file?.absoluteFile?.parentFile?.let { File(it, path) }
        val inWorkingDirectory = File(System.getProperty("user.dir"), path)
        val found = listOfNotNull(besideFile, inWorkingDirectory).firstOrNull { it.isFile }
            ?: throw EcError(location, "File not found: '$path' (looked in ${listOfNotNull(besideFile?.parent, inWorkingDirectory.parent).distinct().joinToString(" and ")}).")
        return found.canonicalPath
    }

    fun getStream(location: Loc, relativePath: String): InputStream {
        if (relativePath.contains(":")) {
            val split = relativePath.split(":")
            if (split.size > 2) throw EcError(location, "Malformed path '$relativePath'")
            if (split[0] == "core") {
                val path = split[1]
                return Util::class.java.classLoader.getResourceAsStream(path)
                    ?: throw EcError(location, "File not found at path '$relativePath'")
            }
            else throw EcError(location, "Unknown path prefix '${split[0]}' at '$relativePath'")
        }

        return FileInputStream(File(relativePath))
    }



    private val tupleEntryNames: Array<String> = arrayOf("first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth", "ninth", "tenth",
        "eleventh", "twelfth", "thirteenth", "fourteenth", "fifteenth", "sixteenth", "seventeenth", "seventeenth", "eighteenth", "nineteenth", "twentieth")
    fun getTupleEntryName(index: Int, length: Int) = if (index < tupleEntryNames.size) tupleEntryNames[index] else "entry$index"
}