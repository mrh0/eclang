// error: Type Hidden is private to module 'crate'
use "../pass/lib/crate.ec"

fn main(): Int do
    val h = crate.Hidden<Int> { 1 }
    ret 0
