package github.mrh0.eclang

import github.mrh0.eclang.antlr.EclangLexer
import github.mrh0.eclang.antlr.EclangParser
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Visitor
import github.mrh0.eclang.ast.token.TProgram
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.file.Path

object Compiler {
    fun tokenizeFile(file: File): TProgram {
        val stream = FileInputStream(file)
        val input = ANTLRInputStream(stream)

        val lexer = EclangLexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = EclangParser(tokens)

        return Visitor(file).visitProgram(parser.program()) as TProgram
    }

    fun tokenizeStream(stream: InputStream, filename: String): TProgram {
        val input = ANTLRInputStream(stream)

        val lexer = EclangLexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = EclangParser(tokens)

        return Visitor(File(filename)).visitProgram(parser.program()) as TProgram
    }
}