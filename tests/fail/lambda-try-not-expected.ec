// error: This lambda uses `try`, so it can fail with Error, but here a (String) => Long is expected, which can't fail.
use "core:string.ec"
fn main(): Int do
    val f: (String) => Long = fn (s) = try parseInt(s)
    ret f("1") as Int
