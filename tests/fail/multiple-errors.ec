// error: Cannot use 3 as Bool
fn first(): Int do
    val x: Int = "text"c
    ret x

// Reported too, though the function above already failed.
fn second() do
    val b: Bool = 3

fn main(): Int do
    ret 0
