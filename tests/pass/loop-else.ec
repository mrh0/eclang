use "core:io.ec"

// A loop's `else` block runs when the loop ends without `break`.

fn find(values: Int[], wanted: Int) do
    for value in values do
        if value == wanted do
            io.log("found"c)
            break
    else do
        io.log("not found"c)

fn countdown(from: Int) do
    var n = from
    while n > 0 do
        n = n - 1
        break if n == 2
    else
        io.log("reached zero"c)
    io.log(n)

fn main(): Int do
    find([1, 2, 3], 2)
    find([1, 2, 3], 7)
    countdown(1)
    countdown(5)

    // A `break` in the else block belongs to the outer loop.
    var rounds = 0
    while true do
        rounds = rounds + 1
        for i in 0..<2 do
            continue
        else do
            break if rounds == 3
    io.log(rounds)

    for i in 0..<0 do
        io.log("never"c)
    else do
        io.log("empty range"c)
    ret 0
