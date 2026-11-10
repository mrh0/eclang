// error: Can't use the pool 'p' here: from the second round of the loop on, `destroy p` at line 5 has freed it
fn main(): Int do
    val p = new Pool
    for i in 0..<3 do
        destroy p
    ret 0
