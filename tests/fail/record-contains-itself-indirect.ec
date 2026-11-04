// error: Record A contains itself (through A.b.a)
use "core:lib.ec"

rec A as
    x: Int
    b: B

rec B as
    a: A?

rec Node as
    next: @Node?
    kids: Node[]

fn main(): Int do
    ret 0
