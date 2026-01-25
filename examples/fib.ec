module "fib"

use "core:lib.ec"

val a = 0i
val b = 1i

fn main(): Int do
	for var i in 0 to 100 do
		a = b
		b = a + b
		log(a)
	ret 0
