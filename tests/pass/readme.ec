// The examples from README.md, so the documentation stays true.
use "core:io.ec"

declare fn abs extern "fabs" as (x: Double): Double
declare type DefinedType as Int

rec Test as
    a: Int
    b: Int

rec Duck as
    name: CString
    coolness: Int

rec Goose as
    name: CString
    loudness: Int

fn test(a: Int, b: Int | CString, c: Int = a): Int do
    ret c

fn x(a: Int) do
    io.log("Int"c)

fn x(a: CString) do
    io.log("Str"c)

fn y(a: Int | CString) do
    x(a)

fn varArgTest(a: Int, b: Int, ...rest: String): Int do
    ret a + b

fn varArgCTest(a: Int, b: Int, ...): Int do
    ret a + b

fn testCallSigFunc(v: Int): Int do
    ret v * 2

fn throwsTest(i: Int): Int throws Error do
    throw Error { 1, "Number is too large" } if i > 10
    ret i + 1

fn n(a: CString?): CString do
    ret a ?? ""c

fn a(atom: :log | :dont_log) do
    io.log("logged"c) if atom == :log

fn quack(bird: Duck | Goose) do
    io.log(bird.name)

fn generic(value: <B>) do
    io.log(value)

fn defTest(input: DefinedType): Int do
    defer io.log("1"c)
    io.log("2"c)
    if true do
        defer io.log("3"c)
        io.log("4"c)
    else
        defer io.log("5"c)
        io.log("test"c)
        ret 1
    ret input

fn main(): Int do
    io.log("Hello World")
    // Variables and numbers
    val tchar: Char = 10b
    val tshort: Short = 10s
    val tint: Int = 10i
    val tlong: Long = 10l
    val tuint: UInt = 10ui
    val tulong: ULong = 10ul
    val f: Float = 6f
    vol volatileValue: Int = 0
    io.log(tshort + tint)
    io.log(tlong + tulong as Long)
    io.log(tuint)
    io.log(f)
    io.log(tchar as Int)
    // Strings, arrays, atoms
    val cstring2: CString = "Hello World"c
    val array1 = ["One", "Two", "Three"]
    val array2: Int[] = [5, 3, 6, 0]
    val carray2: Int[]c = [5, 3, 6, 0]c
    io.log(array1.len)
    for item in array1 do
        io.log(item)
    for index, item in array1 do
        io.log(index)
        io.log(item)
    io.log(array2[2] + carray2[1])
    val k = :test_atom
    io.log(k)
    // Operators
    val ca: CString = "Hello"c
    val cb: CString = "Hello"c
    io.log(ca == cb)
    io.log(2 ** 10)
    // Functions
    io.log(test(1, 2))
    io.log(test(1, "two"c, 3))
    y(5)
    y("five"c)
    10.x()
    io.log(varArgTest(5, 8, "Hello", "World", "!"))
    io.log(varArgCTest(5, 8, "Hello"c, 1.5))
    val ref = fn testCallSigFunc
    io.log(abs(-2.5))
    // Records
    val t1: Test = { 0, 0 }
    val t2 = Test { 1, 2 }
    io.log(t1.a + t2.b)
    // Control flow
    var iter = 3
    while !!iter do
        io.log("I"c)
        iter = iter - 1
    io.log("positive"c) if iter == 0
    io.log(if iter > 0 do 1 else 0)
    // Errors
    io.log(throwsTest(5) ?? 0)
    io.log(throwsTest(50) ?? 0)
    // Types
    val intValue: Int = 56
    val longValue: Long = intValue as Long
    io.log(longValue)
    val variable: Int = 10
    val addr: @Int = addrof variable
    val otherVariable: Int = @addr
    io.log(otherVariable)
    io.log(n(null))
    io.log(n("set"c))
    a(:log)
    a(:dont_log)
    quack(Duck { "Donald"c, 9 })
    quack(Goose { "Gerald"c, 11 })
    generic("Hello"c)
    generic(420)
    io.log(defTest(7))
    ret 0
