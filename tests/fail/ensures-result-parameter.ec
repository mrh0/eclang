// error: a parameter can't be called 'result'. Rename the parameter.
fn f(result: Int): Int ensures result > 0 = result
fn main(): Int do
    ret f(1)
