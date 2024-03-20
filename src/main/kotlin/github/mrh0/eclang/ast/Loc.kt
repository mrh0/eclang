package github.mrh0.eclang.ast

import org.antlr.v4.runtime.Token
import java.io.File

class Loc(val token: Token?, private val file: File?) {
    companion object {
        val IDENTITY = Loc(null, null)
    }

    fun getFilename(): String = file?.name ?: ""
    fun getLine(): Int = token?.line ?: 0
    fun getRow(): Int = token?.charPositionInLine ?: 0

    override fun toString(): String {
        return "[${file?.absolutePath ?: ""}:${getLine()}:${getRow()+1}]"
    }
}