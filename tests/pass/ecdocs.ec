// Ecdocs can go anywhere, even inside indented blocks, without changing what the program does.

/** The ecdoc before the first `use` describes the file. */
use "core:io.ec"

/** A pair of numbers. */
rec Pair as
    /** The first. */
    a: Int

    /**
     * The second.
     */
    b: Int = 2

/** Something with a size. */
inter Sized as
    /** How many. */
    size: Int

/**
 * Adds the two numbers of [p].
 * @param p the pair
 * @return a + b
 */
fn sum(p: Pair): Int do
    /* An ordinary block comment. */
    val total = p.a + p.b
    ret total

/** Says hello. */
fn main(): Int do
    io.log(sum(Pair{a = 1}))
    ret 0
