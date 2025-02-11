use "core:lib.ec"

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

fn main(): Int do
    varArgTest(5, 8, "Hello", "World", "!")
    throwsTest1(7)
    throwsTest2(7)
    log(genericTest("Test"c))
    createPool(addrof(ROOT_POOL!), Null)
    log("Cloned String"c.clone())
    val array1 = ["One", "Two", "Three"]
    val array2: Int[] = [5, 3, 6, 0]
    val carray1 = ["One", "Two", "Three"]c
    val carray2: Int[]c = [5, 3, 6, 0]c
    const staticVal = 6
    log(array1.len) if true
    for index in array1 do
        log(array1[index])
    log("Hello World")
    val tchar: Char = 10c
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
    log("Hello World"c)
    log("World"c)
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
        log("Test1"c)
    else if false do
        log("Test2"c)
    else
        log("Test3"c)
    ret if (true) 1 else 0

declare type DefinedType as Int

fn defTest(input: DefinedType): Int do
    defer log("1"c)
    log("2"c)
    if true do
        defer log("3"c)
        log("4"c)
    else
        defer do
            log("5"c)
        log(
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
    log("Int"c)

fn x(a: CString) do
    log("Str"c)

fn y(a: Int | CString) do
    x(a)

fn z(a: Int): Int = a + 1
