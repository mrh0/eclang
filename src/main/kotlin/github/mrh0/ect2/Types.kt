package github.mrh0.ect2

import github.mrh0.ect2.Category.*

/**
 * The built-in types. They are shared by every [TypeContext] and have fixed ids `0 until ALL.size`.
 * Their names are the eclang spellings, and [ALL] is the order they are registered in.
 */
object Types {
    private val list = mutableListOf<Prim>()
    private fun prim(name: String, category: Category, bits: Int = 0) = Prim(list.size, name, category, bits).also { list += it }

    /** Top type: every value is an Any. Has no C representation of its own. */
    val Any = prim("Any", TOP)
    /** Bottom type: no value has it (e.g. the type of `throw`). Fits anywhere. */
    val Never = prim("Never", BOTTOM)
    /** No value (`void` in C). */
    val None = prim("None", UNIT)
    /** The type of `null`. Only fits where a nullable type (`T?`) is expected. */
    val Null = prim("Null", NULL)
    val Bool = prim("Bool", BOOL)

    val Char = prim("Char", SINT, 8)
    val Short = prim("Short", SINT, 16)
    val Int = prim("Int", SINT, 32)
    val Long = prim("Long", SINT, 64)
    val UChar = prim("UChar", UINT, 8)
    val UShort = prim("UShort", UINT, 16)
    val UInt = prim("UInt", UINT, 32)
    val ULong = prim("ULong", UINT, 64)
    val Size = prim("Size", UINT, 64)
    val Float = prim("Float", FLOAT, 32)
    val Double = prim("Double", FLOAT, 64)

    /** The type of every atom (`:name`). */
    val Atom = prim("Atom", ATOM)
    val String = prim("String", STRING)
    val CString = prim("CString", CSTRING)

    val ALL: List<Prim> = list.toList()
    val signedIntegers = listOf(Char, Short, Int, Long)
    val unsignedIntegers = listOf(UChar, UShort, UInt, ULong, Size)
    val integers = signedIntegers + unsignedIntegers
    val numbers = integers + listOf(Float, Double)
}
