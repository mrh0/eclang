// error: there is nothing for `debug` to leave out of a release build
fn main(): Int do
    debug const assert 1 < 2
    ret 0
