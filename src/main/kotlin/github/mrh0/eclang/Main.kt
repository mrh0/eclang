package github.mrh0.eclang

import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.output.c.CSource
import github.mrh0.eclang.types.EcTypeNone
import github.mrh0.eclang.types.TypeRegistry
import github.mrh0.eclang.util.Util
import org.apache.commons.cli.*
import java.io.FileInputStream
import java.nio.file.Path
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

const val VERSION = "0.1.0"
var DEBUG = false

val logo = """
    ______________                     
   / ____/ ____/ /   ____ _____  ____ _
  / __/ / /   / /   / __ `/ __ \/ __ `/
 / /___/ /___/ /___/ /_/ / / / / /_/ / 
/_____/\____/_____/\__\_/_/ /_/\__, /  
                              /____/   
""".trimIndent()

val logoSmall = """
   ___________                 
  / __/ ___/ /  ___ ____  ___ _
 / _// /__/ /__/ _ `/ _ \/ _ `/
/___/\___/____/\_\_/_//_/\_, / 
                        /___/  
""".trimIndent()

@OptIn(ExperimentalTime::class)
fun main(args: Array<String>) {
    val options = Options()
    options.addOption(Option.builder("c").longOpt("compile").argName("file").type(FileInputStream::class.java).hasArg().desc("File to compile, must have a main function").build())
    options.addOption(Option.builder("o").longOpt("output").argName("path").type(Path::class.java).hasArg().desc("Output .c source file path and name").build())
    options.addOption("v", "version", false, "Prints version")
    options.addOption("d", "debug", false, "Prints debug info")
    options.addOption("dc", "declaration-comments", false, "Includes declaration comments in output") // TODO
    options.addOption("od", "only-declarations", false, "Emits declarations only") // TODO
    options.addOption("t", "time", false, "Prints compile time")
    options.addOption("h", "help", false, "Prints help")
    val parser: CommandLineParser = DefaultParser()

    val cmd: CommandLine = parser.parse(options, args)
    if (cmd.hasOption("v")) println("v$VERSION")
    DEBUG = cmd.hasOption("d")
    if (cmd.hasOption("h") || cmd.options.isEmpty()) {
        println(logoSmall)
        println("ECLang 'Easy C Language' v$VERSION (C) MRH0 2026")
        println("Options:")
        options.options.forEach {
            println("\t--${it.longOpt}${if (it.hasArg()) " <${it.argName}> " else " "}(-${it.opt}) ${it.description}.")
        }
    }
    // -c test.ec -d -v -t -h -o out.c

    if (!cmd.hasOption("c")) return

    val file = Util.getPath(Loc.IDENTITY, cmd.getOptionValue("c", "")).toFile()
    // val file = Path.of(Util::class.java.classLoader.getResource("test.ec")!!.toURI()).toFile()

    val timeTaken = measureTime {
        TypeRegistry.registerBuiltin()

        val tree: ITok = Compiler.tokenizeFile(file)
        if (DEBUG) {
            println("-Tokens:")
            println(tree)
        }

        val cd = CompileData()
        val (_, ir) = tree.process(cd, EcTypeNone)
        if (DEBUG) {
            println("-Intermediate:")
            println(ir)
        }
        cd.finalize()

        val out = CSource().build(ir)

        if (DEBUG) {
            println("-Output:")
            println(out)
        }

        Util.getPath(Loc.IDENTITY, cmd.getOptionValue("o", "./out.c")).toFile().writeText(out)
    }
    if (cmd.hasOption("t")) println(timeTaken);
}
