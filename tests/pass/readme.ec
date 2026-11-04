// The examples from README.md, so the documentation stays true.
use "core:lib.ec"

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
    log("Int"c)

fn x(a: CString) do
    log("Str"c)

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
    log("logged"c) if atom == :log

fn quack(bird: Duck | Goose) do
    log(bird.name)

fn generic(value: <B>) do
    log(value)

fn defTest(input: DefinedType): Int do
    defer log("1"c)
    log("2"c)
    if true do
        defer log("3"c)
        log("4"c)
    else
        defer log("5"c)
        log("test"c)
        ret 1
    ret input

fn main(): Int do
    log("Hello World")
    // Variables and numbers
    val tchar: Char = 10b
    val tshort: Short = 10s
    val tint: Int = 10i
    val tlong: Long = 10l
    val tuint: UInt = 10ui
    val tulong: ULong = 10ul
    val f: Float = 6f
    vol volatileValue: Int = 0
    log(tshort + tint)
    log(tlong + tulong as Long)
    log(tuint)
    log(f)
    log(tchar as Int)
    // Strings, arrays, atoms
    val cstring2: CString = "Hello World"c
    val array1 = ["One", "Two", "Three"]
    val array2: Int[] = [5, 3, 6, 0]
    val carray2: Int[]c = [5, 3, 6, 0]c
    log(array1.len)
    for item in array1 do
        log(item)
    for index, item in array1 do
        log(index)
        log(item)
    log(array2[2] + carray2[1])
    val k = :test_atom
    log(k)
    // Operators
    val ca: CString = "Hello"c
    val cb: CString = "Hello"c
    log(ca == cb)
    log(2 ** 10)
    // Functions
    log(test(1, 2))
    log(test(1, "two"c, 3))
    y(5)
    y("five"c)
    10.x()
    log(varArgTest(5, 8, "Hello", "World", "!"))
    log(varArgCTest(5, 8, "Hello"c, 1.5))
    val ref = fn testCallSigFunc
    log(abs(-2.5))
    // Records
    val t1: Test = { 0, 0 }
    val t2 = Test { 1, 2 }
    log(t1.a + t2.b)
    // Control flow
    var iter = 3
    while !!iter do
        log("I"c)
        iter = iter - 1
    log("positive"c) if iter == 0
    log(if iter > 0 do 1 else 0)
    // Errors
    log(throwsTest(5) ?? 0)
    log(throwsTest(50) ?? 0)
    // Types
    val intValue: Int = 56
    val longValue: Long = intValue as Long
    log(longValue)
    val variable: Int = 10
    val addr: @Int = addrof variable
    val otherVariable: Int = @addr
    log(otherVariable)
    log(n(null))
    log(n("set"c))
    a(:log)
    a(:dont_log)
    quack(Duck { "Donald"c, 9 })
    quack(Goose { "Gerald"c, 11 })
    generic("Hello"c)
    generic(420)
    log(defTest(7))
    ret 0
