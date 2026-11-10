// panic: wrong user 7
use "core:io.ec"

fn main(): Int do
    val id = 7
    val name = "bob"
    assert name == "ann", "wrong user ${id}"
    ret 0
