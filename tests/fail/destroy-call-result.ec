// error: This `destroy` frees a pool this function didn't make with `new Pool`: it is the result of an expression
fn make(): Pool = new Pool

fn main(): Int do
    destroy make()
    ret 0
