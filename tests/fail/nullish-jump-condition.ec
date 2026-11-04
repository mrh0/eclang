// error: `??` with a jump can't be used here
fn find(): Int? = null

fn main(): Int do
    while (find() ?? ret 0) > 1 do
        pass
    ret 0
