use "core:lib.ec"

fn describe(name: CString?): CString do
    ret "nobody"c if name == null
    // name is a CString from here on.
    ret name

fn length(name: CString?): Int do
    if name != null do
        ret 1
    ret 0

fn show(v: Int | CString) do
    log(v)

fn kind(v: Int | CString | Null): CString do
    if v == null do
        ret "null"c
    else if v is Int do
        ret "int"c
    ret "string"c

fn main(): Int do
    log(describe(null))
    log(describe("Ada"c))
    log(length("x"c))
    show(5)
    show("five"c)

    var u: Int | CString = 42
    if u is Int do
        log(u + 1)
    u = "now a string"c
    if u is CString do
        log(u)

    log(kind(null))
    log(kind(3))
    log(kind("s"c))

    val maybe: Int? = 10
    val nothing: Int? = null
    log(maybe ?? 0)
    log(nothing ?? -1)
    val m2 = maybe ?? 0
    log(m2 + 1)
    log(!!maybe)
    log(!nothing)
    if maybe != null and maybe > 5 do
        log("big"c)
    log(maybe!)
    log(u as CString)
    ret 0
