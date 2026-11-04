// error: The value of constant 'NOW' must be known at compile time: a literal, another constant, or arithmetic on them. For a value computed when the program runs, use `val`.
fn now(): Int = 3

const NOW = now()

fn main(): Int do
    ret 0
