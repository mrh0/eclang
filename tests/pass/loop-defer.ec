use "core:io.ec"

// `break` and `continue` run the statements deferred inside the loop body, like the end of a round does.
fn main(): Int do
    while true do
        defer io.log("left the loop"c)
        break
    for i in 0..<3 do
        defer io.log("end of round"c)
        continue if i == 1
        io.log(i)
    // Only the defers inside the loop run; the function's own run when it returns.
    defer io.log("end of main"c)
    var n = 0
    while n < 5 do
        defer io.log("outer round"c)
        n = n + 1
        if n == 2 do
            defer io.log("inner block"c)
            continue
        break if n == 3
    io.log(n)
    ret 0
