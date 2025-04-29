package github.mrh0.ect2

import github.mrh0.ect2.core.Types2Any
import github.mrh0.ect2.core.Types2None
import github.mrh0.ect2.core.Types2Type
import github.mrh0.ect2.core.Types2TypeVector
import github.mrh0.ect2.types.Types2Char
import github.mrh0.ect2.types.Types2Integer
import github.mrh0.ect2.types.Types2Number
import github.mrh0.ect2.types.Types2String

fun test(title: String, cond: Boolean, expected: Boolean) {
    if (cond == expected) println("PASS $title")
    else println("FAIL $title")
}

fun main(args: Array<String>) {
    test("Integer is a Integer",
        Types2Integer.accepts(Types2Integer), true)
    test("Integer is a Number",
        Types2Number.accepts(Types2Integer), true)
    test("None is not a Number",
        Types2Number.accepts(Types2None), false)
    test("None is Any",
        Types2Any.accepts(Types2None), true)
    test("String is Char Vector",
        Types2TypeVector.of(Types2Char).accepts(Types2String), true)
    test("Char Vector is not a String",
        Types2String.accepts(Types2TypeVector.of(Types2Char)), false)
    test("String can be cast to Char Vector",
        Types2String.cast(Types2TypeVector.of(Types2Char)).accepts(Types2TypeVector.of(Types2Char)), true)
    println(Types2Integer.union(Types2String).union(Types2None).union(Types2Any))

    println(Types2Type.ALL_TYPES.keys)
}