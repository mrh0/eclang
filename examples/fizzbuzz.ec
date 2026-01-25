use "core:lib.ec"

fn main(): Int do
    for i in 0 to 100 do
        if i % 3 == 0 && i % 5 == 0 do
            log("FizzBuzz"c)
        else if i % 3 == 0 do
            log("Fizz"c)
        else if i % 5 == 0 do
            log("Buzz"c)
        else
            log(i)
    ret 0
