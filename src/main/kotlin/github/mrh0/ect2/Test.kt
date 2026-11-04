package github.mrh0.ect2

import github.mrh0.ect2.TypeContext.Resolution
import github.mrh0.ect2.Types.Any
import github.mrh0.ect2.Types.Atom
import github.mrh0.ect2.Types.CString
import github.mrh0.ect2.Types.Char
import github.mrh0.ect2.Types.Double
import github.mrh0.ect2.Types.Float
import github.mrh0.ect2.Types.Int
import github.mrh0.ect2.Types.Long
import github.mrh0.ect2.Types.Never
import github.mrh0.ect2.Types.None
import github.mrh0.ect2.Types.Null
import github.mrh0.ect2.Types.Short
import github.mrh0.ect2.Types.Size
import github.mrh0.ect2.Types.String
import github.mrh0.ect2.Types.UInt
import github.mrh0.ect2.Types.ULong
import kotlin.system.exitProcess

private var failures = 0

fun test(title: String, actual: Any?, expected: Any?) {
    if (actual == expected) println("PASS $title")
    else { println("FAIL $title: expected $expected, got $actual"); failures++ }
}

fun main() {
    val t = TypeContext()

    // Basics
    test("Int is an Int", t.accepts(Int, Int), true)
    test("Int is a Number", t.accepts(t.number, Int), true)
    test("None is not a Number", t.accepts(t.number, None), false)
    test("None is a None", t.accepts(None, None), true)
    test("None is Any", t.accepts(Any, None), true)
    test("Never fits anywhere", t.accepts(Int, Never), true)
    val id = t.distinct("", "UserId", Int)
    test("a distinct type doesn't accept its base", t.accepts(id, Int), false)
    test("a distinct type doesn't fit its base", t.accepts(Int, id), false)
    test("a distinct type accepts a number literal", t.accepts(id, t.intLiteral(5)), true)
    test("a literal prefers the base of a distinct type", t.cost(t.intLiteral(5), Int) < t.cost(t.intLiteral(5), id), true)
    test("two distinct types of one base differ", t.accepts(id, t.distinct("", "OrderId", Int)), false)
    test("String converts to Char[]", t.accepts(t.array(Char), String), true)
    test("Char[] is not a String", t.accepts(String, t.array(Char)), false)

    // Numbers: only lossless implicit conversions
    test("Short -> Int", t.accepts(Int, Short), true)
    test("Long -> Int needs 'as'", t.accepts(Int, Long), false)
    test("Int -> UInt needs 'as'", t.accepts(UInt, Int), false)
    test("UInt -> Long", t.accepts(Long, UInt), true)
    test("ULong -> Size", t.accepts(Size, ULong), true)
    test("Int -> Double", t.accepts(Double, Int), true)
    test("Int -> Float needs 'as' (would lose precision)", t.accepts(Float, Int), false)
    test("Long -> Double needs 'as' (would lose precision)", t.accepts(Double, Long), false)

    // Unions (regressions from the old implementation)
    val intOrStr = t.union(Int, CString) as Union
    test("Union accepts its members", t.accepts(intOrStr, Int), true)
    test("Number does not accept Int | CString", t.accepts(t.number, intOrStr), false)
    test("A | B is B | A", t.union(CString, Int) === intOrStr, true)
    test("Union is interned", t.union(Int, CString) === intOrStr, true)
    test("Nested unions are flattened", t.union(t.union(Int, CString), Char) === t.union(Char, Int, CString), true)
    test("Union with Any is Any", t.union(Int, Any), Any)
    test("Union of one is that type", t.union(Int, Int), Int)
    test("Atom swallows atom literals", t.union(t.atom("a"), Atom), Atom)
    test("Adding to a union does not change it", t.union(intOrStr, Short).let { intOrStr.members }, listOf(Int, CString))
    test("Int | CString fits Int | CString | Char", t.accepts(t.union(Int, CString, Char), intOrStr), true)

    // Nullability
    val nullableStr = t.nullable(CString)
    test("Null does not fit CString", t.accepts(CString, Null), false)
    test("Null fits CString?", t.accepts(nullableStr, Null), true)
    test("CString? does not fit CString", t.accepts(CString, nullableStr), false)
    test("CString?! is CString", t.nonNull(nullableStr), CString)
    test("Nullable prints as T?", nullableStr.toString(), "CString?")

    // Structural types
    test("Tuples are ordered", t.tuple(listOf(Int, Char)) === t.tuple(listOf(Char, Int)), false)
    test("Tuples match exactly", t.accepts(t.tuple(listOf(Long, Int)), t.tuple(listOf(Int, Int))), false)
    test("A tuple is a union member", t.accepts(t.nullable(t.tuple(listOf(Int, Int))), t.tuple(listOf(Int, Int))), true)
    test("Int[] is not a Long[] (different layout)", t.accepts(t.array(Long), t.array(Int)), false)
    test("@Int -> @Any (void*)", t.accepts(t.pointer(Any), t.pointer(Int)), true)
    test("@Int is not @Long", t.accepts(t.pointer(Long), t.pointer(Int)), false)

    // Records and interfaces
    val animal = t.record("main", "Animal").apply { define(mapOf("name" to CString)) }
    val cat = t.record("main", "Cat", animal).apply { define(mapOf("purring" to Int)) }
    val named = t.interfaceType("main", "Named").apply { define(mapOf("name" to CString)) }
    test("Cat is an Animal", t.accepts(animal, cat), true)
    test("Animal is not a Cat", t.accepts(cat, animal), false)
    test("@Cat -> @Animal", t.accepts(t.pointer(animal), t.pointer(cat)), true)
    test("Cat inherits fields", cat.fields.keys.toList(), listOf("name", "purring"))
    test("Cat has the fields of Named", t.accepts(named, cat), true)
    test("@Cat -> @Named", t.accepts(t.pointer(named), t.pointer(cat)), true)
    test("@Int is not a @Named", t.accepts(t.pointer(named), t.pointer(Int)), false)
    test("@Named can't be stored", t.storageProblem(t.pointer(named)) != null, true)
    test("Records with equal fields are still different types",
        t.accepts(animal, t.record("main", "Other").apply { define(mapOf("name" to CString)) }), false)

    // Overload resolution
    val overloads = listOf(listOf(Int), listOf(Long), listOf(Double))
    test("f(Short) picks f(Int)", (t.resolve(overloads, listOf(Short)) { it } as Resolution.Found).match, listOf(Int))
    test("f(UInt) picks f(Long)", (t.resolve(overloads, listOf(UInt)) { it } as Resolution.Found).match, listOf(Long))
    test("f(CString) has no match", t.resolve(overloads, listOf(CString)) { it }, Resolution.NoMatch)
    test("Equal costs are ambiguous",
        t.resolve(listOf(listOf(Int, Long), listOf(Long, Int)), listOf(Short, Short)) { it } is Resolution.Ambiguous, true)

    // Generics
    val tp = t.typeParam("T")
    val bindings = mutableMapOf<TypeParam, Type>()
    test("<T>[] binds to Int[]", t.bind(t.array(tp), t.array(Int), bindings), true)
    test("T is Int", bindings[tp], Int)
    test("Substitute (T, @T)", t.substitute(t.tuple(listOf(tp, t.pointer(tp))), bindings), t.tuple(listOf(Int, t.pointer(Int))))
    test("<T> already Int rejects CString", t.bind(tp, CString, bindings), false)

    // Literals take the type they are used as
    val five = t.intLiteral(5)
    test("5 fits Char", t.accepts(Char, five), true)
    test("300 does not fit Char", t.accepts(Char, t.intLiteral(300)), false)
    test("-1 does not fit UInt", t.accepts(UInt, t.intLiteral(-1)), false)
    test("5 on its own is an Int", t.concrete(five), Int)
    test("5000000000 on its own is a Long", t.concrete(t.intLiteral(5_000_000_000)), Long)
    test("2.5 on its own is a Double", t.concrete(t.floatLiteral(2.5)), Double)
    test("2.5 fits Float", t.accepts(Float, t.floatLiteral(2.5)), true)
    test("f(5) prefers f(Int) over f(Long) and f(Char)",
        (t.resolve(listOf(listOf(Char), listOf(Long), listOf(Int)), listOf(five)) { it } as Resolution.Found).match, listOf(Int))
    test("Generic <T> bound by a literal is Int", mutableMapOf<TypeParam, Type>().also { t.bind(tp, five, it) }[tp], Int)

    // Results (calls that can fail) must be handled
    val result = t.result(Int, CString)
    test("A result is not its value", t.accepts(Int, result), false)
    test("Results are interned", t.result(Int, CString) === result, true)

    // Explanations say what to do
    test("Explain narrowing", t.explain(Long, Int), "Long → Int may lose data (Int is smaller); convert it explicitly with `as Int`")
    test("Explain literal range", t.explain(t.intLiteral(300), Char), "300 does not fit in Char (-128 to 127)")
    test("Explain null", t.explain(Null, CString), "null is not allowed here. To allow it, make the type nullable: `CString?`")
    test("Explain interface", t.explain(t.record("main", "Rock").apply { define(mapOf("w" to Int)) }, named),
        "Rock is missing `name: CString`, required by Named")

    // Some types only work as parameters
    test("Any can't be stored", t.storageProblem(Any) != null, true)
    test("@Any can be stored", t.storageProblem(t.pointer(Any)), null)
    test("CString fits @Any (void*)", t.accepts(t.pointer(Any), CString), true)
    test("@Any is not a CString", t.accepts(CString, t.pointer(Any)), false)
    test("An interface can't be stored", t.storageProblem(named) != null, true)

    // Contexts are independent
    test("Separate contexts don't share records", TypeContext().lookup("main", "Cat"), null)
    test("Names fall back to core", t.lookup("main", "Int"), Int)
    val topPoint = t.record("", "Point").apply { define(mapOf("x" to Int)) }
    val geoPoint = t.record("geo", "Point").apply { define(mapOf("lat" to Double)) }
    test("A module sees its own names first", t.lookup("geo", "Point"), geoPoint)
    test("A module sees the top level", t.lookup("main", "Point"), topPoint)
    test("Other modules' names are not visible", t.lookup("main", "Cat") === cat && t.lookup("other", "Cat") == null, true)
    test("Qualified lookup", t.lookupIn("geo", "Point"), geoPoint)
    test("Records in a module get a prefixed C name", geoPoint.cName to topPoint.cName, "geo__Point" to "Point")

    // Rough speed check: repeated comparisons are cache hits.
    val big = t.union(Types.numbers + listOf(CString, String, t.pointer(Int)))
    val start = System.nanoTime()
    var hits = 0
    repeat(1_000_000) { if (t.accepts(big, Types.numbers[it % Types.numbers.size])) hits++ }
    println("1M accepts() on a 14-member union: ${(System.nanoTime() - start) / 1_000_000} ms")

    println(if (failures == 0) "All tests passed" else "$failures test(s) FAILED")
    if (failures > 0) exitProcess(1)
}
