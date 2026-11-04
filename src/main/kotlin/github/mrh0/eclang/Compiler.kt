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
import github.mrh0.eclang.ir.IRProgram
import github.mrh0.eclang.output.c.CSource
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
    fun compile(file: File, configure: (CompileData) -> Unit = {}): Compilation {
        val cd = CompileData().also(configure)
        // An error that escapes every recovery point stops the compilation; it is reported like the others.
        fun stop(e: EcError) { try { cd.report(e) } catch (_: EcAbort) {} }
        try {
            val tree = tokenizeStream(cd.unsaved(Util.sourceName(file)) ?: FileInputStream(file), file.path)
            if (DEBUG) println("-Tokens:\n$tree")
            val ir = tree.process(cd, Types.None).second
            if (DEBUG) println("-Intermediate:\n$ir")
            if (cd.errors.isEmpty()) return Compilation(cd, CSource(cd.types, cd.functions.printers, cd.library != null).build(ir), (ir as IRProgram).header(cd.types))
        } catch (e: EcError) {
            stop(e)
        } catch (_: EcAbort) {}
        return Compilation(cd, null)
    }

    fun tokenizeFile(file: File): TProgram = tokenizeStream(FileInputStream(file), file.path)

    fun tokenizeStream(stream: InputStream, filename: String): TProgram {
        val parsed = parse(stream, filename)
        return (Visitor(parsed.file).visitProgram(parsed.tree) as TProgram).also { it.docs = parsed.docs }
    }

    /** A parsed file: its parse tree, and its declarations with their ecdocs. */
    class Parsed(val file: File, val tree: EclangParser.ProgramContext, val docs: FileDocs)

    /** Parses [stream] (the file [filename]); a syntax error is thrown as an [EcError]. */
    fun parse(stream: InputStream, filename: String): Parsed {
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

        val tree = parser.program()
        return Parsed(file, tree, DocExtractor(file, lexer.ecdocs, lexer.strayEcdocs).extract(tree))
    }

    /** Parses [text], the expression inside `${...}` of a string literal at [at]; its tokens all report [at]. */
    fun parseInterpolation(text: String, at: Loc): ITok {
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
        return Visitor(at.file ?: File(""), at).visit(parser.interpolation().expr())
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