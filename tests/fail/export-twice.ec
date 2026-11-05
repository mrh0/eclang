// error: The C name 'f' of the exported function 'f' is already used
export fn f(a: Int): Int = a
export fn f(a: Double): Double = a

fn main(): Int do
    ret 0
