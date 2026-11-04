use "core:c/stdio.ec"

rec Point as
    x: Int

/** No `hash` function exists at all here: `hash` is the built-in one. */
fn main(): Int do
    printf("%d\n"c, if hash(Point { 1 }) == hash(Point { 1 }) do 1 else 0)
    printf("%d\n"c, if hash(Point { 1 }) == hash(Point { 2 }) do 1 else 0)
    ret 0
