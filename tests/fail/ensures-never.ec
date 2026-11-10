// error: an `ensures` would never be checked
fn stop(): Never ensures 1 > 0 do
    panic("stop")
fn main(): Int do
    stop()
