// error: Record A contains itself (through A.a), so it would be infinitely large. Make A a `ref rec A`

rec A as
    a: A

fn main(): Int do
    ret 0
