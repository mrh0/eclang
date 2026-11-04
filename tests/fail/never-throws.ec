// error: 'f' returns Never, so it never comes back
use "core:errors.ec"

fn f(): Never throws Error do
    panic("x")

fn main(): Int do
    ret 0
