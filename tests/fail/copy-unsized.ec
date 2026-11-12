// error: Can't `copy` a @Any: eclang doesn't know the size of what a @Any points to
fn main() do
    val n = new 5
    val p = n as unsafe @Any
    val q = copy(p)
