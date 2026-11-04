use "core:io.ec"

fn main(): Int do
    for i in 0..<100 do
        if i % 3 == 0 && i % 5 == 0 do
            io.log("FizzBuzz"c)
        else if i % 3 == 0 do
            io.log("Fizz"c)
        else if i % 5 == 0 do
            io.log("Buzz"c)
        else
            io.log(i)
    ret 0
