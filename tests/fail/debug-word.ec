// error: `trace` before a statement isn't anything eclang knows. Only `debug` can be written there
use "core:io.ec"
fn main(): Int do
    trace io.log("x")
    ret 0
