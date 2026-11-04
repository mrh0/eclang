// error: A tuple can't hold this: Any has no fixed size
fn f(t: (Any, Int)): Int = t.second

fn main(): Int = f((1, 2))
