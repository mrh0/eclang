package github.mrh0.eclang

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.output.c.CSource
import github.mrh0.eclang.types.EcTypeNone
import github.mrh0.eclang.util.Util
import org.apache.commons.cli.*
import java.io.FileInputStream
import java.nio.file.Path
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

const val VERSION = "0.1.0"
var DEBUG = false

@OptIn(ExperimentalTime::class)
fun main(args: Array<String>) {
    val options: Options = Options()
    options.addOption(Option.builder("c").longOpt("compile").type(FileInputStream::class.java).hasArg().build())
    options.addOption(Option.builder("o").longOpt("output").type(Path::class.java).hasArg().build())
    options.addOption("v", "version", false, "Prints version")
    options.addOption("d", "debug", false, "Prints debug info")
    options.addOption("t", "time", false, "Prints compile time")
    options.addOption("h", "help", false, "Prints help")
    val parser: CommandLineParser = DefaultParser()
    val cmd: CommandLine = parser.parse(options, args)

    if (cmd.hasOption("v")) println("v$VERSION")
    DEBUG = cmd.hasOption("d")

    val file = Util.getPath(Loc.IDENTITY, cmd.getOptionValue("c")).toFile()
    // val file = Path.of(Util::class.java.classLoader.getResource("test.ec")!!.toURI()).toFile()

    val timeTaken = measureTime {
        val tree: ITok = Compiler.tokenizeFile(file)
        if (DEBUG) {
            println("--Tokens:")
            println(tree)
        }

        val cd = CompileData()
        val (_, ir) = tree.process(cd, EcTypeNone)
        if (DEBUG) {
            println("--Intermediate:")
            println(ir)
        }
        cd.finalize()

        val out = CSource().build(ir)

        if (DEBUG) {
            println("--Output:")
            println(out)
        }

        Util.getPath(Loc.IDENTITY, cmd.getOptionValue("o", "./out.c")).toFile().writeText(out)
    }
    if (cmd.hasOption("t")) println(timeTaken);
}
