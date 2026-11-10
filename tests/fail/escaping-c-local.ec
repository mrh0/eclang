// error: 'keep_pointer' keeps 'p' after it returns (it is `escaping`
// A C function that keeps a pointer is declared with `escaping`, which each call checks.
declare fn keep_pointer extern "free" as (escaping p: @Int)

fn main() do
    var x = 1
    keep_pointer(addrof x)
