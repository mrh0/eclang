use "core:lib.ec"

// `while true` without `break` never ends normally, so no `ret` is needed after it.
fn firstOver(limit: Int): Int do
    var i = 0
    while true do
        i = i + 7
        ret i if i > limit

fn main(): Int do
    log(firstOver(20))
    ret 0
