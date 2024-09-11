package github.mrh0.eclang.util

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcProtectedPrefixError

object Util {
    fun getStringContent(token: String) = token.substring(1, token.length-1)

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
}