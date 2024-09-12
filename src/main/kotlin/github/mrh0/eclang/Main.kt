package github.mrh0.eclang

import github.mrh0.eclang.antlr.EclangLexer
import github.mrh0.eclang.antlr.EclangParser
import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Visitor
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.output.c.CSource
import github.mrh0.eclang.types.EcTypeNone
import github.mrh0.eclang.util.Util
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import java.io.File
import java.io.FileInputStream
import java.nio.file.Path
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
fun main(args: Array<String>) {
    val file = Path.of(Util::class.java.classLoader.getResource("test.ec")!!.toURI()).toFile()

    val timeTaken = measureTime {
        val tree: ITok = Compiler.tokenizeFile(file)
        println(tree)

        val cd = CompileData()
        val (_, ir) = tree.process(cd, EcTypeNone)
        println(ir)
        cd.finalize()

        val out = CSource().build(ir)

        println("Output:")
        println(out)

        File("./out.c").writeText(out)
    }
    println(timeTaken);
}
