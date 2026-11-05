// error: Can't use the pool 'p' here: from the second round of the loop on, `freePool(p)` at line 5 has freed it
fn main(): Int do
    val p = newPool()
    for i in 0..<3 do
        freePool(p)
    ret 0
