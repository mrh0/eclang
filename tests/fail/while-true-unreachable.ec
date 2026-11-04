// error: This code can never run
fn f(): Int do
    while true do
        ret 1
    ret 2

fn main(): Int do
    ret f()
