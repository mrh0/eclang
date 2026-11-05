// error: Can't store this in 'saved': it points to memory from the pool of the `pool` block
fn g(): Int throws String do
    throw "failed ${1}"
fn main(): Int do
    var saved = ""
    pool do
        try g() catch e do
            saved = e
    ret saved.len as Int
