package github.mrh0.eclang.ast

import org.antlr.v4.runtime.Token
import java.io.File

class Loc(val token: Token?, val file: File?, private val line: Int? = null, private val column: Int? = null) {
    companion object {
        val IDENTITY = Loc(null, null)
    }

    fun getFilename(): String = file?.name ?: ""
    fun getLine(): Int = line ?: token?.line ?: 0
    fun getCharPos(): Int = column ?: token?.charPositionInLine ?: 0

    override fun toString(): String {
        return "${file?.path ?: "<unknown>"}:${getLine()}:${getCharPos()+1}"
    }
}