use "core:io.ec"

fn label(n: Int): String do
    when n do
        10..>0 do ret "low"
        20..>10 do ret "mid"
        else do ret "other"

fn main() do
    for i in 5..>0 do
        io.log(i)
    val n: Size = 3
    for i in n..>0 do
        io.log(i)
    for i in 2..>-2 do
        io.log(i)
    for i in 0..>0 do
        io.log("never")
    io.log(label(0))
    io.log(label(1))
    io.log(label(10))
    io.log(label(11))
    io.log(label(20))
    io.log(label(21))
