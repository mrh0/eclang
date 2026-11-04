package github.mrh0.eclang.ast

import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.ect2.Type

/**
 * An expression that names a place in memory, so it can be assigned: a variable, a field (`p.x`), an element
 * (`a[i]`) or what a pointer points to (`@p`).
 */
interface IAssignable {
    /** The place, as its type and a C lvalue. */
    fun place(cd: CompileData): Place
}

/**
 * A place in memory of type [type], written in C as [ir]. [readOnly] is null when it may be changed, otherwise
 * why it can't, as the end of a sentence ("'p' is a `val`"). [name] is the local variable it is, for narrowing.
 */
data class Place(val type: Type, val ir: IIR, val readOnly: String? = null, val name: String? = null)

/** This expression without the parentheses around it. */
fun ITok.unnested(): ITok = if (this is github.mrh0.eclang.ast.token.TExprNest) expr.unnested() else this
