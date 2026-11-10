// gcc: const-assert-sizeof.ec:7: Header is written to disk as it is: const assert sizeof Header == 12
// Only C knows the size of a struct, so this `const assert` is a `_Static_assert`, which gcc checks.
rec Header as
    magic: UInt
    size: UInt

const assert sizeof Header == 12, "Header is written to disk as it is"

fn main(): Int do
    ret 0
