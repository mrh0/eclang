use "ec:lib.ec"

fn main(): Int do
    for i in 0 to 100 do
        if i % 3 == 0 && i % 5 == 0 do
            log(c"FizzBuzz")
        else if i % 3 == 0 do
            log(c"Fizz")
        else if i % 5 == 0 do
            log(c"Buzz")
        else
            log(i)
    ret 0
