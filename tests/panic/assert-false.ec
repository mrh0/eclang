// panic: assert-false.ec:5: unreachable
fn pick(n: Int): Int do
    if n > 0 do
        ret n
    assert false, "unreachable"

fn main(): Int do
    ret pick(-1)
