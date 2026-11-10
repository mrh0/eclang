// error: `destroy gp` frees a pool this function didn't make with `new Pool`: 'gp' is a global, which other functions may use too
var gp = new Pool

fn main(): Int do
    destroy gp
    ret 0
