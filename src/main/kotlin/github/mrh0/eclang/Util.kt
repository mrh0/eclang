package github.mrh0.eclang

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcProtectedPrefixError

object Util {
    fun getStringContent(token: String) = token.substring(1, token.length-1)

    fun testIdentifier(location: Loc, id: String) {
        if (id.startsWith("__ec_")) throw EcProtectedPrefixError(location)
    }
}