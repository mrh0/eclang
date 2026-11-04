// error: it never returns, so it can't `ret`
fn f(): Never do
    ret 1

fn main(): Int do
    f()
