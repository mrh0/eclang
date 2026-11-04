// error: Function f is declared `Never`, so it must not reach its end
fn f(): Never do
    val x = 1

fn main(): Int do
    f()
