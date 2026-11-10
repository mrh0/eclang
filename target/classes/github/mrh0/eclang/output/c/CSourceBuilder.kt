package github.mrh0.eclang.output.c

import github.mrh0.eclang.output.BlockScope
import github.mrh0.eclang.output.SourceBuilder
import java.util.*

class CSourceBuilder(val types: CTypes) : SourceBuilder() {
    val scopeStack: Stack<BlockScope> = Stack()

    fun putStatement() {
        for (i in 1..scopeStack.size) put('\t')
    }

    fun putStatement(line: String) {
        putStatement()
        put(line)
    }

    fun endStatement() = put(";\n")

    fun pushScope(scope: BlockScope) {
        scopeStack.push(scope)
        putLine("{")
    }

    fun popScope(es: Boolean = false, extra: String = ""): BlockScope {
        for (i in 1 until scopeStack.size) put('\t')
        if(es) putLine("};") else putLine("}$extra")
        return scopeStack.pop()
    }

    fun comment(comment: String) {
        put("/* ")
        put(comment)
        put(" */")
    }

    fun commentLine(comment: String) {
        putStatement()
        put("// ")
        put(comment)
        put("\n")
    }

    /** Writes [write] into a separate builder (same types and indentation) and returns the text. */
    fun render(write: (CSourceBuilder) -> Unit): String {
        val inner = CSourceBuilder(types)
        scopeStack.forEach { inner.scopeStack.push(it) }
        write(inner)
        return inner.toString()
    }
}
