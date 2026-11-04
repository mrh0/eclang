// error: 'f' takes 1 argument ((Int) => Int), but 2 were given.
fn apply(f: (Int) => Int): Int = f(1, 2)

fn main(): Int do
    ret 0
