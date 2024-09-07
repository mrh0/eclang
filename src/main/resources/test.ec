extern fn "printf" as log(value: String): None
extern fn "printf" as log(format: String, value: String): None

rec Test as
    a: Int
    b: Int

fn main(): Int do
    0.x()
    log "Hello World"
    log ("Hello %s", "World")
    test(0, "")
    y("")
    z(1)
    val k = :test_atom
    a(aa())
    if "Test" == "NotTest" do
        val ff: String = "DFE"
        log "Test1"
    else if false do
        log "Test2"
    else
        log "Test3"
    ret if (true) 1 else 0

fn n(a: Int?): Int? do
    ret a

fn aa(): :Test1 = :Test1

fn a(atom: :Test1 | :Test2 | :Test3): Atom do
    ret atom

fn test(a: Int, b: Int & String, c: Int = a): Int do
    val d: Int = 5
    ret c

fn x(a: Int) do
    log "Int"

fn x(a: String) do
    log "Str"

fn y(a: Int & String) do
    x(a)

fn z(a: Int): Int = a + 1
