// error: `nodebug requires` isn't anything eclang knows
fn f(x: Int): Int nodebug requires x > 0 do
    ret x
fn main() do
    f(1)
