module "test"

use "core:io.ec"

val G: Int = 69
var G2: Int = 420

rec Test as
    a: Int
    b: Int

rec UnionTest as
    a: Int
    b: Number

val unionTest: UnionTest = { 0, 0i }

fn genericTest(test: <A>): <A> do
    val v: <A> = ""c
    ret v

fn throwsTest1(i: Int): Int throws Error do
    ret i+1

fn throwsTest2(i: Int): Int throws Error do
    throw Error { 0, "Test" }

fn varArgTest(a: Int, b: Int, ...c: String): Int do
    ret a + b

fn varArgCTest(a: Int, b: Int, ...): Int do
    ret a + b

fn testCallSigFunc(v: Int): Int do
    ret 1

//fn funcWithCallSig(func: (Int) => Int): Int do
//    ret 1

fn main(): Int do
    //funcWithCallSig(fn testCallSigFunc)
    varArgTest(5, 8, "Hello", "World", "!")
    varArgCTest(5, 8, "Hello", "World", "!")
    throwsTest1(7)
    throwsTest2(7)
    // val testUnitValue = 554.0testUnit
    val testCast = "Testing" as unsafe CString
    io.log(genericTest("Test"c))
    createPool(addrof(ROOT_POOL!), null)
    io.log("Cloned String"c.clone())
    val array1 = ["One", "Two", "Three"]
    val array2: Int[] = [5, 3, 6, 0]
    val carray1 = ["One", "Two", "Three"]c
    val carray2: Int[]c = [5, 3, 6, 0]c
    const staticVal = 6
    io.log(array1.len) if true
    for index in array1 do
        io.log(array1[index])
    io.log("Hello World")
    val tchar: Char = 10b
    val tshort: Short = 10s
    val tint: Int = 10i
    val tlong: Long = 10l
    //val uchar: UChar = 10uc
    val tushort: UShort = 10us
    val tuint: UInt = 10ui
    val tulong: ULong = 10ul
    val a: CString = "Hello"c
    val b: CString = "Hello"c
    val c: Bool = a == b // true
    val d: Bool = a === b // false
    val divRes: IntDivResult = div(245, 5)
    0.x()
    io.log("Hello World"c)
    io.log("World"c)
    val addr: @Int = addrof G2
    val addr2: Int = @addr
    test(
        0,
        ""c
    )
    val testRec: Test = { 0, 0 }
    val ee = testRec.a
    y(""c)
    z(1)
    val k = :test_atom
    val p = G + G2
    val f: Float = 6f
    val char: Char = 'a'
    val char2: Char = '\r'
    a(aa())
    n("Null"c)
    defTest(7)
    var iter = 5
    while !!iter do
        log ("I"c)
        iter = iter-1
    if "Test"c == "NotTest"c do
        val ff: CString = "DFE"c
        io.log("Test1"c)
    else if false do
        io.log("Test2"c)
    else
        io.log("Test3"c)
    ret if (true) 1 else 0

declare type DefinedType as Int

fn defTest(input: DefinedType): Int do
    defer io.log("1"c)
    io.log("2"c)
    if true do
        defer io.log("3"c)
        io.log("4"c)
    else
        defer do
            io.log("5"c)
        io.log(
            "test"c
        )
        ret 1
    ret input

fn n(a: CString?): CString do
    ret a ?? ""c

fn n(a = 0): Int do
    ret 0

fn aa(): :Test1 = :Test1

fn a(atom: :Test1 & :Test2 & :Test3): Atom do
    ret atom

fn test(a: Int, b: Int | CString, c: Int = a): Int do
    val d: Int = 5
    ret c

fn x(a: Int) do
    io.log("Int"c)

fn x(a: CString) do
    io.log("Str"c)

fn y(a: Int | CString) do
    x(a)

fn z(a: Int): Int = a + 1
