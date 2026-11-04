// The type-system examples from README.md (literals, flow typing, records, errors, casts, interfaces, generics).
use "core:io.ec"

rec Test as
    a: Int
    b: Int

rec Animal as
    name: CString

rec Cat extends Animal as
    lives: Int

inter Named as
    name: CString

rec Duck as
    name: CString
    coolness: Int

fn greet(who: Named) do
    io.log(who.name)

fn greeting(name: CString?): CString do
    ret "Hello, stranger"c if name == null
    ret name                    // name is a CString here

fn describe(v: Int | CString | Null): CString do
    ret "nothing"c if v == null      // v is Int | CString from here on
    if v is Int do
        ret "a number"c              // v is an Int here
    ret v                            // and a CString here

fn throwsTest(i: Int): Int throws Error do
    throw Error { 1, "Number is too large" } if i > 10
    ret i + 1

fn check(v: Int) throws Error do      // returns nothing, but may fail
    throw Error { 2, "negative" } if v < 0

fn twice(i: Int): Int throws Error do
    val n = try throwsTest(i)
    ret n * 2

fn first(values: <T>[]): <T> = values[0]

fn main(): Int do
    val c: Char = 65
    val big = 5000000000
    val ratio = 2.5
    val f: Float = 2.5
    val folded: Char = 60 + 6
    val t3 = Test { b = 2, a = 1 }
    val tom = Cat { name = "Tom"c, lives = 9 }
    var pet: @Animal = addrof tom
    if pet is @Cat do
        io.log(pet.lives)
    val cat = pet as @Cat
    io.log(greeting(null))
    io.log(describe(null))
    io.log(describe(5))
    io.log(describe("text"c))
    val result = throwsTest(0) ?? 10
    io.log(result)
    io.log(twice(3) ?? 0)
    try throwsTest(50) catch error do
        io.log(error.message)
    try check(-1) catch e do
        io.log(e.code)
    val intValue: Int = 56
    val longValue: Long = intValue as Long
    val small = longValue as Short
    greet(Duck { "Donald"c, 9 })
    io.log(first([7, 8]))
    io.log(t3.a)
    ret 0
