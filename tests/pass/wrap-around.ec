use "core:io.ec"

fn next(i: Int): Bool = i + 1 > i

fn main() do
    io.log(next(2147483647))
    io.log(next(1))
