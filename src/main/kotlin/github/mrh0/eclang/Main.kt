package github.mrh0.eclang

import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.util.Util
import org.apache.commons.cli.*
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime
import kotlin.system.exitProcess

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
    if (args.firstOrNull() == "run") exitProcess(run(args.drop(1)))

    val options = Options()
    options.addOption(Option.builder("c").longOpt("compile").argName("file").type(FileInputStream::class.java).hasArg().desc("File to compile, must have a main function").build())
    options.addOption(Option.builder("o").longOpt("output").argName("path").type(Path::class.java).hasArg().desc("Output .c source file path and name").build())
    options.addOption("v", "version", false, "Prints version")
    options.addOption("d", "debug", false, "Prints debug info")
    options.addOption("dc", "declaration-comments", false, "Includes declaration comments in output") // TODO
    options.addOption("od", "only-declarations", false, "Emits declarations only") // TODO
    options.addOption("t", "time", false, "Prints compile time")
    options.addOption("nb", "no-bounds-check", false, "Doesn't check array and String indices at runtime")
    options.addOption("h", "help", false, "Prints help")
    val parser: CommandLineParser = DefaultParser()

    val cmd: CommandLine = parser.parse(options, args)
    if (cmd.hasOption("v")) println("v$VERSION")
    DEBUG = cmd.hasOption("d")
    if (cmd.hasOption("h") || cmd.options.isEmpty()) {
        println(logoSmall)
        println("ECLang 'Easy C Language' v$VERSION (C) MRH0 2026")
        println("Usage:")
        println("\teclang -c <file.ec> [-o out.c]   Compiles to C.")
        println("\teclang run <file.ec> [-nb]       Compiles, builds with $CC and runs the program.")
        println("Options:")
        options.options.forEach {
            println("\t--${it.longOpt}${if (it.hasArg()) " <${it.argName}> " else " "}(-${it.opt}) ${it.description}.")
        }
    }
    // -c test.ec -d -v -t -h -o out.c

    if (!cmd.hasOption("c")) return

    val file = sourceFile(cmd.getOptionValue("c", ""))
    val timeTaken = measureTime {
        val out = compileOrExit(file, !cmd.hasOption("nb"))
        if (DEBUG) {
            println("-Output:")
            println(out)
        }
        Util.getPath(Loc.IDENTITY, cmd.getOptionValue("o", "./out.c")).toFile().writeText(out)
    }
    if (cmd.hasOption("t")) println(timeTaken);
}

/** The C compiler `run` uses: `$CC`, or gcc. */
private val CC = System.getenv("CC")?.takeIf { it.isNotBlank() } ?: "gcc"

private fun sourceFile(path: String): File {
    val file = Util.getPath(Loc.IDENTITY, path).toFile()
    if (!file.isFile) {
        System.err.println("error: file not found: $file")
        exitProcess(1)
    }
    return file
}

/** Compiles [file] to C, printing its warnings; prints every error and exits when there are any. */
private fun compileOrExit(file: File, boundsChecks: Boolean): String {
    val result = Compiler.compile(file) { it.boundsChecks = boundsChecks }
    val cd = result.cd
    cd.warnings.forEach { System.err.println("${it.location}: warning: ${it.message}") }
    cd.errors.forEach {
        System.err.println("${it.location}: error: ${it.reason}")
        if (DEBUG) it.printStackTrace()
    }
    if (result.c != null) return result.c
    val count = cd.errors.size
    if (count >= CompileData.MAX_ERRORS) System.err.println("Stopped after $count errors; fix these first.")
    else if (count > 1) System.err.println("$count errors.")
    exitProcess(1)
}

/**
 * `eclang run file.ec`: compiles the program, builds it with the C compiler (with APR and the math library), runs it,
 * and exits with its exit code. The C file and the executable go to a temporary directory.
 */
private fun run(args: List<String>): Int {
    val options = Options()
    options.addOption("nb", "no-bounds-check", false, "Doesn't check array and String indices at runtime")
    options.addOption("d", "debug", false, "Prints debug info")
    options.addOption(Option.builder("k").longOpt("keep").argName("dir").hasArg().desc("Keeps the C file and executable in this directory").build())
    val cmd = try { DefaultParser().parse(options, args.toTypedArray()) } catch (e: ParseException) {
        System.err.println("error: ${e.message}"); return 2
    }
    val path = cmd.argList.singleOrNull() ?: run {
        System.err.println("Usage: eclang run <file.ec> [-nb] [-k dir]")
        return 2
    }
    DEBUG = cmd.hasOption("d")
    val file = sourceFile(path)
    val c = compileOrExit(file, !cmd.hasOption("nb"))

    val dir = cmd.getOptionValue("k")?.let { File(it).apply { mkdirs() } } ?: Files.createTempDirectory("eclang").toFile().apply { deleteOnExit() }
    val name = file.nameWithoutExtension
    val source = File(dir, "$name.c").apply { writeText(c) }
    val exe = File(dir, name)
    val keep = cmd.hasOption("k")
    try {
        val gcc = listOf(CC, source.path, "-o", exe.path) + aprFlags() + "-lm"
        if (DEBUG) println(gcc.joinToString(" "))
        val built = try { ProcessBuilder(gcc).inheritIO().start().waitFor() } catch (e: IOException) {
            System.err.println("error: couldn't start the C compiler '$CC' (${e.message}). Install gcc, or set CC to your C compiler.")
            return 1
        }
        if (built != 0) {
            System.err.println("error: the C compiler failed on the generated code${if (keep) " ($source)" else ""}. This is a bug in eclang if the program compiled without errors; please report it.")
            return 1
        }
        return ProcessBuilder(exe.path).inheritIO().start().waitFor()
    } finally {
        if (!keep) { source.delete(); exe.delete(); dir.delete() }
    }
}

/** The compiler and linker flags for APR, from `apr-1-config` when it is installed. */
private fun aprFlags(): List<String> {
    fun config(flag: String): List<String>? = try {
        val p = ProcessBuilder("apr-1-config", flag).redirectErrorStream(true).start()
        val out = p.inputStream.bufferedReader().readText()
        if (p.waitFor() == 0) out.trim().split(Regex("\\s+")).filter { it.isNotEmpty() } else null
    } catch (_: IOException) { null }
    return (config("--includes") ?: listOf("-I/usr/include/apr-1.0")) + (config("--link-ld") ?: listOf("-lapr-1"))
}
