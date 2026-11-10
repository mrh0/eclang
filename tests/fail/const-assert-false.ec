// error: `const assert SLOTS <= 255` failed: it is false, as SLOTS = 300.
const SLOTS = 300
const assert SLOTS <= 255, "a slot index is a UChar"
fn main(): Int do
    ret 0
