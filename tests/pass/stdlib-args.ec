use "core:io.ec"

fn main(): Int do
    // Run without arguments by the tests; tests/run.sh checks them with `eclang run ... -- a b`.
    val all = io.args()
    io.log(all.len)
    for a in all do
        io.log(a)
    io.log(io.programName().len > 0)
    ret 0
