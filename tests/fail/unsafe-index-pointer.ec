// error: `[]` on a @Int isn't checked: it has no length
fn third(p: @Int): Int do
    ret p[2]

fn main() do
    ret
