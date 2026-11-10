// error: This `assert` always fails: the types or constants already decide it.
const LIMIT = 4
fn main(): Int do
    assert LIMIT > 9
    ret 0
