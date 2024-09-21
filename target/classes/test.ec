use "lib.ec"

val G: Int = 69
var G2: Int = 420

rec Test as
    a: Int
    b: Int

fn main(): Int do
    val a: CString = "Hello"
    val b: CString = "Hello"
    val c: Bool = a == b // true
    val d: Bool = a === b // false
    val divRes: IntDivResult = div(245, 5)
    0.x()
    log "Hello World"
    log ("Hello %s", "World")
    val addr: @Int = addrof G2
    val addr2: Int = @addr
    test(
        0,
        ""
    )
    val testRec: Test = { 0, 0 }
    val ee = testRec.a
    y("")
    z(1)
    val k = :test_atom
    val p = G + G2
    val f: Float = 6f
    val char: Char = 'a'
    val char2: Char = '\r'
    a(aa())
    n("Null")
    defTest(7)
    if "Test" == "NotTest" do
        val ff: CString = "DFE"
        log "Test1"
    else if false do
        log "Test2"
    else
        log "Test3"
    ret if (true) 1 else 0

declare type DefinedType = Int

fn defTest(input: DefinedType): Int do
    defer log("1")
    log("2")
    if true do
        defer log("3")
        log("4")
    else
        defer log("5")
        log(
            "test"
        )
        ret 1
    ret input

fn n(a: CString?): CString do
    ret a ?? ""

fn n(a: Int): Int do
    ret 0

fn aa(): :Test1 = :Test1

fn a(atom: :Test1 | :Test2 | :Test3): Atom do
    ret atom

fn test(a: Int, b: Int & CString, c: Int = a): Int do
    val d: Int = 5
    ret c

fn x(a: Int) do
    log "Int"

fn x(a: CString) do
    log "Str"

fn y(a: Int & CString) do
    x(a)

fn z(a: Int): Int = a + 1
