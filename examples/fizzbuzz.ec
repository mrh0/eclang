use "ec:lib.ec"

fn main(): Int do
	for var i in 0..100 do
		if i % 3 == 0 && i % 5 == 0 do
			log("FizzBuzz\n")
		else if i % 3 == 0 do
			log("Fizz\n")
		else if i % 5 == 0 do
            log("Buzz\n")
		else
			log("%d\n", i)
		}
	}
	ret 0
