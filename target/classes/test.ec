use "lib.ec"
declare fn "printf" as log(value: CString): None
declare fn "printf" as log(format: CString, value: CString): None

val G: Int = 69
var G2: Int = 420

rec Test as
    a: Int
    b: Int

fn main(): Int do
    0.x()
    log "Hello World"
    log ("Hello %s", "World")
    test(
        0,
        ""
    )
    otherFileFunc()
    val testRec: Test = { 0, 0 }
    y("")
    z(1)
    val k = :test_atom
    val p = G + G2
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

type DefinedType = Int

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
