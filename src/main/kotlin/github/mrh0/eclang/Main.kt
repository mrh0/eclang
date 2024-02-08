package github.mrh0.eclang

import Root
import github.mrh0.eclang.antlr.EclangLexer
import github.mrh0.eclang.antlr.EclangParser
import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Visitor
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.SourceBuilder
import github.mrh0.eclang.output.c.CSourceBuilder
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import java.io.FileInputStream
import java.nio.file.Path
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
fun main(args: Array<String>) {
    val file = Path.of(Root::class.java.classLoader.getResource("test.gs")!!.toURI()).toFile()

    val timeTaken = measureTime {
        val stream = FileInputStream(file)
        val input = ANTLRInputStream(stream)

        val lexer = EclangLexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = EclangParser(tokens)

        val tree: ITok = Visitor(file).visitProgram(parser.program())
        println(tree)

        val cd = CompileData()
        val (_, ir) = tree.process(cd)
        println(ir)
        cd.finalize()

        val outputBuilder = CSourceBuilder();
        ir.toC(outputBuilder, Context.IDENTITY)
        println(outputBuilder.toString())
    }
    println(timeTaken);
}
