// error: `caller` gives a parameter the place of the call
fn f(at: Double = caller) do
    ret

fn main() do
    f()
