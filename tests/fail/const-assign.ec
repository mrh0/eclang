// error: 'LIMIT' is a `const`
const LIMIT = 10

fn main(): Int do
    LIMIT = 11
    ret 0
