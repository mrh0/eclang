use "core:io.ec"

rec Pair as
    a: Char
    b: Long

fn sign(x: Int): Int = if x > 0 do 1 else if x < 0 do -1 else 0

fn main(): Int do
    // `**` binds tighter than prefix `-`, as in maths and Python.
    io.log(-2 ** 2)
    val x = 3
    io.log(-x ** 2)
    io.log((-x) ** 2)
    io.log(2 ** 3 ** 2)
    io.log(2.0 ** -1)
    var p: Int = 2
    val ptr = addrof p
    io.log(@ptr ** 3)
    // The inline `if` uses `do`, like the statement.
    io.log(if x > 2 do "big"c else "small"c)
    io.log(sign(-5))
    io.log(sign(0))
    io.log(sign(8))
    // Type suffixes.
    io.log(65b + 1b)
    // Layout of types.
    io.log(alignof Long)
    io.log(sizeof Pair)
    io.log(offsetof(Pair, b))
    io.log(offsetof(Pair, a))
    // `assert` stops the program when its condition is false; afterwards the condition is known.
    val maybe: Int? = 41
    assert maybe != null
    io.log(maybe + 1)
    assert x == 3, "x should be three"
    ret 0
