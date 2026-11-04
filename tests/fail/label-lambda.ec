// error: `break` can only be used inside a loop
fn run(f: () => None) do
    f()

fn stop() do
    break

fn main(): Int do
    outer: for i in 0..<3 do
        run(fn stop)
    ret 0
