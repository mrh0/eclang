package github.mrh0.eclang

import github.mrh0.eclang.antlr.EclangLexer
import github.mrh0.eclang.antlr.EclangParser
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Visitor
import github.mrh0.eclang.ast.token.TProgram
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.doc.DocExtractor
import github.mrh0.eclang.doc.FileDocs
import github.mrh0.eclang.error.EcAbort
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.error.EcMemoryError
import github.mrh0.eclang.ir.IRProgram
import github.mrh0.eclang.output.c.CSource
import github.mrh0.eclang.util.StringLiteral
import github.mrh0.eclang.util.Util
import github.mrh0.ect2.Types
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.CommonTokenStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.file.Path

object Compiler {
    /** The result of compiling a program: its C source, or null when there were errors (listed in `cd.errors`). [header]: a library's C header. */
    class Compilation(val cd: CompileData, val c: String?, val header: String? = null)

    /**
     * Compiles the program [file] (and the files it uses) to C. Each call has its own [CompileData], so one JVM can
     * compile many times (a watch mode, a language server). [configure] sets options such as bounds checks.
     */
    fun compile(file: File, configure: (CompileData) -> Unit = {}): Compilation = onLargeStack { compileHere(file, configure) }

    /** How often a program is compiled at most, with what the one before found that functions return (see [compileHere]). */
    private const val MAX_PASSES = 4

    /**
     * Compiles [file]. Until every body is compiled, a call's result may point into each of its arguments; a compilation that
     * fails on memory ([EcMemoryError]) is tried again knowing what each function's result really points into (Escapes.resultRoots),
     * as long as that finds more. A program that compiles the first time is compiled once.
     */
    private fun compileHere(file: File, configure: (CompileData) -> Unit): Compilation {
        var known: Map<String, Set<String>> = mapOf()
        var pass = 1
        while (true) {
            val compilation = compileOnce(file, configure, known)
            if (compilation.c != null || pass == MAX_PASSES || compilation.cd.errors.none { it is EcMemoryError }) return compilation
            val found = try { compilation.cd.escapes.computedResults() } catch (_: Exception) { return compilation }
            if (found == known) return compilation
            known = found
            pass++
        }
    }

    private fun compileOnce(file: File, configure: (CompileData) -> Unit, known: Map<String, Set<String>>): Compilation {
        val cd = CompileData().also(configure)
        cd.escapes.knownResults = known
        // An error that escapes every recovery point stops the compilation; it is reported like the others.
        fun stop(e: EcError) { try { cd.report(e) } catch (_: EcAbort) {} }
        try {
            val tree = tokenizeStream(cd.unsaved(Util.sourceName(file)) ?: FileInputStream(file), file.path)
            if (DEBUG) println("-Tokens:\n$tree")
            val ir = tree.process(cd, Types.None).second
            if (DEBUG) println("-Intermediate:\n$ir")
            if (cd.errors.isEmpty()) return Compilation(cd, CSource(cd.types, cd.functions.printers, cd.library != null, cd.checkMemory).build(ir), (ir as IRProgram).header(cd.types))
        } catch (e: EcError) {
            stop(e)
        } catch (_: EcAbort) {
        } catch (_: StackOverflowError) {
            stop(EcError(Loc(null, file, 1, 0), "The program is nested too deeply to compile. Move deeply nested blocks or expressions into functions of their own."))
        }
        return Compilation(cd, null)
    }

    /** The stack of the thread that compiles: the parser and the token tree recurse once per level of nesting. */
    private const val STACK_SIZE = 1L shl 30
    private val onCompilerThread = ThreadLocal.withInitial { false }

    /**
     * Runs [block] on a thread with a large stack ([STACK_SIZE]; the JVM's default of 1 MB lasts for about 700 `else if`s),
     * or directly when this is already such a thread. What [block] throws is thrown here.
     */
    fun <T> onLargeStack(block: () -> T): T {
        if (onCompilerThread.get()) return block()
        var result: Result<T>? = null
        val thread = Thread(null, {
            onCompilerThread.set(true)
            result = runCatching(block)
        }, "eclang-compiler", STACK_SIZE)
        thread.start()
        thread.join()
        return result!!.getOrThrow()
    }

    fun tokenizeFile(file: File): TProgram = tokenizeStream(FileInputStream(file), file.path)

    fun tokenizeStream(stream: InputStream, filename: String): TProgram {
        val parsed = parse(stream, filename)
        return (Visitor(parsed.file).visitProgram(parsed.tree) as TProgram).also { it.docs = parsed.docs }
    }

    /** A parsed file: its parse tree, and its declarations with their ecdocs. */
    class Parsed(val file: File, val tree: EclangParser.ProgramContext, val docs: FileDocs)

    /** Parses [stream] (the file [filename]); a syntax error is thrown as an [EcError]. */
    fun parse(stream: InputStream, filename: String): Parsed = onLargeStack { parseHere(stream, filename) }

    private fun parseHere(stream: InputStream, filename: String): Parsed {
        val file = File(filename)
        val text = stream.readBytes().toString(Charsets.UTF_8)
        val input = ANTLRInputStream(text)
        val failOnError = SyntaxErrors(file, text.lines())

        val lexer = EclangLexer(input)
        lexer.removeErrorListeners()
        lexer.addErrorListener(failOnError)
        val tokens = CommonTokenStream(lexer)
        val parser = EclangParser(tokens)
        parser.removeErrorListeners()
        parser.addErrorListener(failOnError)

        val tree = try { parser.program() } catch (e: EcError) {
            lexer.removeErrorListeners() // From here on the lexer skips what it can't read, as in the port (ls/vscode).
            throw quoteInInterpolation(tokens, file) ?: e
        } catch (_: StackOverflowError) {
            val at = parser.currentToken
            throw EcError(Loc(null, file, at.line, at.charPositionInLine), "This is nested too deeply to parse. A long `else if` chain is the usual cause: use a `when`, or a table (a Map or an array) of the cases.")
        }
        return Parsed(file, tree, DocExtractor(file, lexer.ecdocs, lexer.strayEcdocs).extract(tree))
    }

    /**
     * A `"` inside `${...}` ends the string there (`"a ${f("x")} b"` is the string `"a ${f("`, the name x and the string
     * `")} b"`), and ANTLR reports that wherever its prediction gave up, often lines before. A string with a `${` it doesn't
     * close, and more code after it on its line, is that mistake, so it is reported at the `"` instead.
     */
    private fun quoteInInterpolation(tokens: CommonTokenStream, file: File): EcError? {
        tokens.fill()
        val list = tokens.tokens
        for ((k, t) in list.withIndex()) {
            if (t.type != EclangParser.STRING) continue
            val end = t.text.lastIndexOf('"')
            if (end <= 0 || StringLiteral.unclosedInterpolation(t.text.substring(1, end)) == null) continue
            val next = list.getOrNull(k + 1) ?: continue
            if (next.line != t.line || next.type == EclangParser.NL || next.type == Token.EOF) continue
            return EcError(Loc(null, file, t.line, t.charPositionInLine + end), "This `\"` ends the string, although it is inside `\${...}`: " +
                "a string can't hold another string in `\${...}`. Put the inner text in a variable first (`val text = \"...\"`), and use the variable inside `\${...}`.")
        }
        return null
    }

    /** Parses [text], the expression inside `${...}` of a string literal at [at]; its tokens all report [at]. */
    fun parseInterpolation(text: String, at: Loc, mutNames: Set<String> = setOf()): ITok {
        val errors = object : BaseErrorListener() {
            override fun syntaxError(recognizer: Recognizer<*, *>?, offendingSymbol: Any?, line: Int, column: Int, msg: String?, e: RecognitionException?) {
                throw EcError(at, "Syntax error in `\${$text}`: $msg")
            }
        }
        val lexer = EclangLexer(ANTLRInputStream(text))
        lexer.removeErrorListeners()
        lexer.addErrorListener(errors)
        val parser = EclangParser(CommonTokenStream(lexer))
        parser.removeErrorListeners()
        parser.addErrorListener(errors)
        return Visitor(at.file ?: File(""), at, mutNames).visit(parser.interpolation().expr())
    }

    /**
     * Stops at the first syntax error (the ones after it are mostly ANTLR losing track), reported like every other
     * compile error, with a message from [SyntaxHints] that says how to fix it.
     */
    private class SyntaxErrors(private val file: File, private val lines: List<String>) : BaseErrorListener() {
        override fun syntaxError(recognizer: Recognizer<*, *>?, offendingSymbol: Any?, line: Int, column: Int, msg: String?, e: RecognitionException?) {
            val hint = SyntaxHints.explain(SyntaxHints.Problem(lines, line, column, offendingSymbol as? Token, msg ?: ""))
            throw EcError(Loc(null, file, hint.line, hint.column), hint.message)
        }
    }
}