use "core:lib.ec"

// `break` and `continue` run the statements deferred inside the loop body, like the end of a round does.
fn main(): Int do
    while true do
        defer log("left the loop"c)
        break
    for i in 0..<3 do
        defer log("end of round"c)
        continue if i == 1
        log(i)
    // Only the defers inside the loop run; the function's own run when it returns.
    defer log("end of main"c)
    var n = 0
    while n < 5 do
        defer log("outer round"c)
        n = n + 1
        if n == 2 do
            defer log("inner block"c)
            continue
        break if n == 3
    log(n)
    ret 0
