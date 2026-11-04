// error: Record A contains itself (through A.a), so it would be infinitely large. Store a pointer instead, e.g. `a: @A?`.

rec A as
    a: A

fn main(): Int do
    ret 0
