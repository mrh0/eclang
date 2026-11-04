package github.mrh0.eclang

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
    } finally {
        dir.deleteRecursively()
    }
    if (failed) exitProcess(1)
}
