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
            "default", "if", "static", "while", "do", "int", "struct", "_Packed", "double"
            -> "_$id"
            else -> id
        }
    }

    @Deprecated("Use getStream")
    fun getPath(location: Loc, relativePath: String): Path {
        if (relativePath.contains(":")) {
            val split = relativePath.split(":")
            if (split.size > 2) throw EcError(location, "Malformed path '$relativePath'")
            if (split[0] == "core") {
                val path = split[1]
                println(path)
                println(Util::class.java.classLoader.getResource(path))
                return Path.of(Util::class.java.classLoader.getResource(path)?.toURI() ?: throw EcError(location, "File not found at path '$relativePath'"))
            }
            else throw EcError(location, "Unknown path prefix '${split[0]}' at '$relativePath'")
        }
        val workingDirectory = System.getProperty("user.dir")
        val path = Paths.get(workingDirectory, relativePath)

        return path.toAbsolutePath()
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