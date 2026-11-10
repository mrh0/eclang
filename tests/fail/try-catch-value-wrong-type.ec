// error: Cannot use Null as String (the value after `catch e do`)
use "core:errors.ec"
fn name(): String throws Error = "a"

fn main(): Int do
    val s = try name() catch e do null
    ret 0
