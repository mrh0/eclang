package github.mrh0.eclang

import github.mrh0.eclang.util.Util
import java.io.File
import java.io.InputStream
import java.io.PrintStream

/**
 * `eclang check`: compiles on request for as long as stdin is open, for an editor (see ls/vscode). A JVM takes a while to
 * start and to warm up, so one process serves every check. Nothing is written to disk, and files needn't have a `main`.
 *
 * A request is the line `check <count> <path>`, then `count` unsaved files, each the line `<bytes> <path>` and that many
 * bytes of UTF-8. The answer is one line of JSON:
 *
 *     {"errors":[{"file":"/a.ec","line":1,"column":0,"message":"..."}],"warnings":[...]}
 *
 * with `"internal":"..."` instead when the compiler itself failed. Lines are 1-based and columns 0-based; files are
 * canonical paths, or `core:name.ec`. A bundled file being edited is named `core:name.ec` in the request, with its text as
 * an unsaved file: by its path on disk it would be loaded twice when the files it uses `use` it.
 */
object Checker {
    fun serve(input: InputStream, output: PrintStream): Int {
        System.setOut(System.err) // Only answers go to [output].
        val stream = input.buffered()
        while (true) {
            val request = line(stream) ?: return 0
            val (command, count, path) = request.split(" ", limit = 3).takeIf { it.size == 3 } ?: listOf("", "", "")
            if (command != "check") {
                System.err.println("error: not a request: $request")
                return 2
            }
            val sources = (0 until count.toInt()).associate {
                val (size, file) = (line(stream) ?: return 2).split(" ", limit = 2)
                Util.sourceName(File(file)) to String(stream.readNBytes(size.toInt()), Charsets.UTF_8)
            }
            output.print(answer(File(path), sources) + "\n")
            output.flush()
        }
    }

    private fun answer(file: File, sources: Map<String, String>): String = try {
        val cd = Compiler.compile(file) { it.checkOnly = true; it.sources = sources }.cd
        val errors = cd.errors.map { problem(it.location.file ?: file, it.location.getLine(), it.location.getCharPos(), it.reason) }
        val warnings = cd.warnings.map { problem(it.location.file ?: file, it.location.getLine(), it.location.getCharPos(), it.message) }
        "{\"errors\":[${errors.joinToString(",")}],\"warnings\":[${warnings.joinToString(",")}]}"
    } catch (e: Throwable) { // A bug in the compiler, or a stack overflow, must not end the service.
        "{\"internal\":${json(e.toString())}}"
    }

    private fun problem(file: File, line: Int, column: Int, message: String): String {
        return "{\"file\":${json(Util.sourceName(file))},\"line\":$line,\"column\":$column,\"message\":${json(message)}}"
    }

    private fun json(s: String): String = buildString {
        append('"')
        for (c in s) when {
            c == '"' || c == '\\' -> append('\\').append(c)
            c < ' ' -> append("\\u%04x".format(c.code))
            else -> append(c)
        }
        append('"')
    }

    /** The next line of [stream], without its line break, or null at the end. */
    private fun line(stream: InputStream): String? {
        val bytes = java.io.ByteArrayOutputStream()
        while (true) {
            val b = stream.read()
            if (b < 0) return if (bytes.size() == 0) null else bytes.toString(Charsets.UTF_8)
            if (b == '\n'.code) return bytes.toString(Charsets.UTF_8)
            bytes.write(b)
        }
    }
}
