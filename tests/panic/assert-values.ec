// panic: total(a) = 6
use "core:io.ec"

fn total(a: Int[]): Int do
    var t = 0
    for x in a do
        t += x
    ret t

fn main(): Int do
    val a = [1, 2, 3]
    // The call runs once, into a temporary that the report shows.
    assert total(a) == 7
    ret 0
