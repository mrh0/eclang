use "core:lib.ec"

rec Pair as
    a: Char
    b: Long

fn sign(x: Int): Int = if x > 0 do 1 else if x < 0 do -1 else 0

fn main(): Int do
    // `**` binds tighter than prefix `-`, as in maths and Python.
    log(-2 ** 2)
    val x = 3
    log(-x ** 2)
    log((-x) ** 2)
    log(2 ** 3 ** 2)
    log(2.0 ** -1)
    var p: Int = 2
    val ptr = addrof p
    log(@ptr ** 3)
    // The inline `if` uses `do`, like the statement.
    log(if x > 2 do "big"c else "small"c)
    log(sign(-5))
    log(sign(0))
    log(sign(8))
    // Type suffixes.
    log(65b + 1b)
    // Layout of types.
    log(alignof Long)
    log(sizeof Pair)
    log(offsetof(Pair, b))
    log(offsetof(Pair, a))
    // `assert` stops the program when its condition is false; afterwards the condition is known.
    val maybe: Int? = 41
    assert maybe != null
    log(maybe + 1)
    assert x == 3, "x should be three"
    ret 0
