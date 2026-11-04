// error: tuple part types must match exactly
fn f(): (Int, Int) = (1, 2)

fn g(): (Long, Int) = f()

fn main(): Int = 0
