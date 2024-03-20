package github.mrh0.eclang.output

import java.util.*

open class SourceBuilder {
    val scopeStack: Stack<BlockScope> = Stack()
    val sb: StringBuilder = StringBuilder()
    override fun toString() = sb.toString()

    open fun put(str: String) = sb.append(str)
    open fun put(char: Char) = sb.append(char)
    open fun put(num: Number) = sb.append(num)
}