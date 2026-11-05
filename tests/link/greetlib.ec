// A library that needs a C library: `eclang lib` must link it in.
use "greet.h"
use "lib:greet"

declare fn greet_add as (a: Int, b: Int): Int

export fn addTwo(n: Int): Int = greet_add(n, 2)
