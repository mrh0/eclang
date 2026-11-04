// error: has no parameter called 'timout'. Did you mean 'timeout'?
fn connect(host: CString, timeout: Int = 30): Int = timeout

fn main(): Int do
    ret connect("a"c, timout = 5)
