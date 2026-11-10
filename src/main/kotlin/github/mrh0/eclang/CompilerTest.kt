package github.mrh0.eclang

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.util.Util
import github.mrh0.ect2.test
import java.io.File
import java.nio.file.Files
import kotlin.system.exitProcess

/**
 * Tests of the compiler as a library: several compilations in one JVM (as a watch mode or a language server would
 * run them) must not share state, and one compilation reports the errors of every function. Run by tests/run.sh.
 */
fun main() {
    val dir = Files.createTempDirectory("eclang-test").toFile()
    var failed = false
    fun check(title: String, actual: Any?, expected: Any?) { if (actual != expected) failed = true; test(title, actual, expected) }
    fun program(name: String, code: String) = File(dir, "$name.ec").apply { writeText(code.trimIndent() + "\n") }

    try {
        val a = program("a", """
            fn main(): Int do
                val x = :red
                val y = :green
                ret 0
        """)
        val b = program("b", """
            fn main(): Int do
                val x = :blue
                ret 0
        """)
        val first = Compiler.compile(a).c
        val other = Compiler.compile(b).c
        val again = Compiler.compile(a).c
        check("A program compiles the same way every time", again, first)
        check("Atoms of one compilation don't leak into the next", other?.contains("__ec_atom_red"), false)
        check("A program has its own atoms", other?.contains("__ec_atom_blue"), true)
        // gcc -Wall -Wextra -Werror rejects an unused `static char* const`, such as a member of io.OpenMode nobody uses.
        val unusedAtoms = program("unused-atoms", """
            use "core:io.ec"
            fn main(): Int do
                io.log("hi"c)
                ret 0
        """)
        check("Atoms the C never refers to aren't defined", Compiler.compile(unusedAtoms).c?.contains("__ec_atom_append"), false)

        val exporting = program("exporting", """
            fn f(): Int = 1

            export fn g(): Int = f()
        """)
        check("A program needs a `main`, and the error says how to make a library", Compiler.compile(exporting).cd.errors.single().reason.contains("build it with `eclang lib`"), true)
        val built = Compiler.compile(exporting) { it.library = "exporting" }
        check("A library needs no `main`", built.c?.contains("int main("), false)
        check("A library's header declares what it exports", built.header?.contains("int g(void);"), true)
        check("A library's header has no APR when its functions don't use it", built.header?.contains("apr"), false)
        check("Only what is exported is visible outside a library", built.c?.contains("static int f_0(void)"), true)
        check("A file without exports is no library", Compiler.compile(a) { it.library = "a" }.cd.errors.single().reason.contains("at least one `export fn`"), true)

        val errors = program("errors", """
            fn f(): Int do
                val x: Int = "a"c
                ret x

            fn g(v: Int | CString) do
                val b: Bool = 1

            fn main(): Int do
                undefined()
                ret 0
        """)
        val failing = Compiler.compile(errors)
        check("A program with errors has no C", failing.c, null)
        check("Errors in separate functions are all reported (and once per union copy)", failing.cd.errors.map { it.location.getLine() }, listOf(2, 6, 9))

        val library = program("library", "fn helper(): Int = 1")
        val checked = { file: File, sources: Map<String, String> -> Compiler.compile(file) { it.checkOnly = true; it.sources = sources }.cd.errors.map { it.reason } }
        check("A check doesn't ask for `main`", checked(library, mapOf()), listOf<String>())
        check("Unsaved text is read instead of the file", checked(library, mapOf(library.canonicalPath to "fn helper(): Int = \"a\"c\n")).size, 1)
        val user = program("user", """
            use "library.ec"

            fn main(): Int = helper()
        """)
        check("Unsaved text of a used file counts too", checked(user, mapOf(library.canonicalPath to "fn helper(): Bool = true\n")).size, 1)

        val core = Compiler.compile(File("core:apr/allocator.ec")) { it.checkOnly = true; it.sources = mapOf("core:apr/allocator.ec" to Util.getStream(Loc.IDENTITY, "core:apr/allocator.ec").readBytes().decodeToString()) }
        check("A bundled file can be checked with unsaved text, and is loaded once", core.cd.errors.map { it.reason }, listOf<String>())

        val cycle = program("cycle", """
            val first: Int = readSecond()
            val second: Int = readFirst()
            fn readFirst(): Int = first
            fn readSecond(): Int = second
            fn main(): Int = 0
        """)
        check("Globals that need each other are warned about", Compiler.compile(cycle).cd.warnings.map { it.message.substringBefore(", through") }, listOf("The value of 'second' depends on itself ('first' -> 'second' -> 'first')"))
        val own = program("own", """
            val own: Int = readOwn()
            fn readOwn(): Int = own
            fn main(): Int = 0
        """)
        check("A global that reads itself is warned about", Compiler.compile(own).cd.warnings.size, 1)
        val order = program("order", """
            val first: Int = second()
            val value: Int = 1 + zero()
            fn zero(): Int = 0
            fn second(): Int = value
            fn main(): Int = first
        """)
        val orderC = Compiler.compile(order)
        check("A global is set after what its value needs, without a warning", orderC.c!!.indexOf("value = ") < orderC.c!!.indexOf("first = ") && orderC.cd.warnings.isEmpty(), true)

        // LoopPools: a loop that allocates in the current pool and keeps nothing past a round gets a pool per round.
        val loops = program("loops", """
            var kept: String = ""
            fn label(n: Int): String = "n is ${'$'}{n}"
            fn add(a: Int, b: Int): Int = a + b
            fn keep(s: String) do
                kept = s
            fn helper() do
                for i in 0..<3 do
                    val s = "${'$'}{i}"
            fn main(): Int do
                for i in 0..<3 do
                    val s = "line ${'$'}{i}"
                var last = ""
                for i in 0..<3 do
                    last = "line ${'$'}{i}"
                var j = 0
                while j < 3 do
                    val s = label(j)
                    j += 1
                for i in 0..<3 do
                    j = add(j, i)
                for i in 0..<3 do
                    pool do
                        val s = "x ${'$'}{i}"
                for i in 0..<3 do
                    keep("k ${'$'}{i}")
                for i in 0..<3 do
                    var acc = ""
                    for k in 0..<2 do
                        acc = "${'$'}{acc}${'$'}{k}"
                helper()
                ret j
        """)
        val loopsC = Compiler.compile(loops)
        check("Loops that allocate and keep nothing past a round get a pool per round, in any function",
            loopsC.cd.roundLoops.filter { it.enabled }.map { it.location.getLine() }, listOf(7, 10, 16, 26))
        check("Rounds with a pool of their own are written as such, and nothing is warned about", loopsC.c!!.contains("__ec_pool = __ec_pool_create(__ec_outer") && loopsC.cd.warnings.isEmpty(), true)

        val chain = program("chain", "fn pick(x: Int): Int do\n    if x == 0 do\n        ret 0\n" +
            (1 until 3000).joinToString("") { "    else if x == $it do\n        ret $it\n" } + "    ret -1\n\nfn main(): Int = pick(5)")
        check("A long `else if` chain compiles (the parser recurses once per branch)", Compiler.compile(chain).c?.contains("else if (x==2999)"), true)

        val request = "check 1 ${library.path}\n".toByteArray() + "9 ${library.path}\n".toByteArray() + "fn f( do\n".toByteArray()
        val reply = java.io.ByteArrayOutputStream()
        Checker.serve(request.inputStream(), java.io.PrintStream(reply))
        check("`eclang check` answers with the errors", reply.toString().contains("\"line\":1"), true)
    } finally {
        dir.deleteRecursively()
    }
    if (failed) exitProcess(1)
}
