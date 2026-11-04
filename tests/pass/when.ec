use "core:io.ec"

// `when`: arms `is T do`, values (`null`, atoms, numbers) and `else do`; a variable is narrowed in each arm.

fn describe(v: Int | CString | Null): CString do
    when v do
        is Int do ret "a number"c
        is CString do
            io.log(v)
            ret "a string"c
        null do ret "nothing"c

fn mode(m: :read | :write | :both): Int do
    when m do
        :read do ret 1
        :write, :both do ret 2

fn pick(b: Bool): Int | CString = if b do 5 else "five"c

fn main(): Int do
    io.log(describe(3))
    io.log(describe("hi"c))
    io.log(describe(null))
    io.log(mode(:both))
    when val p = pick(true) do
        is Int do io.log(p + 1)
        is CString do io.log(p)
    when pick(false) do
        is Int do io.log("int"c)
        else do io.log("other"c)
    val n = 7
    when n % 3 do
        0 do io.log("zero"c)
        1, 2 do io.log("one or two"c)
    when n > 5 do
        true do io.log("big"c)
        false do io.log("small"c)
    ret 0
