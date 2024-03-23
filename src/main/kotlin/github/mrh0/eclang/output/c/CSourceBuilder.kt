package github.mrh0.eclang.output.c

import github.mrh0.eclang.output.BlockScope
import github.mrh0.eclang.output.SourceBuilder
import java.util.*

class CSourceBuilder : SourceBuilder() {
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

    fun popScope(): BlockScope {
        putLine("}")
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
}