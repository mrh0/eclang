use "core:io.ec"

fn describe(name: CString?): CString do
    ret "nobody"c if name == null
    // name is a CString from here on.
    ret name

fn length(name: CString?): Int do
    if name != null do
        ret 1
    ret 0

fn show(v: Int | CString) do
    io.log(v)

fn kind(v: Int | CString | Null): CString do
    if v == null do
        ret "null"c
    else if v is Int do
        ret "int"c
    ret "string"c

fn main(): Int do
    io.log(describe(null))
    io.log(describe("Ada"c))
    io.log(length("x"c))
    show(5)
    show("five"c)

    var u: Int | CString = 42
    if u is Int do
        io.log(u + 1)
    u = "now a string"c
    if u is CString do
        io.log(u)

    io.log(kind(null))
    io.log(kind(3))
    io.log(kind("s"c))

    val maybe: Int? = 10
    val nothing: Int? = null
    io.log(maybe ?? 0)
    io.log(nothing ?? -1)
    val m2 = maybe ?? 0
    io.log(m2 + 1)
    io.log(!!maybe)
    io.log(!nothing)
    if maybe != null and maybe > 5 do
        io.log("big"c)
    io.log(maybe!)
    io.log(u as CString)
    ret 0
