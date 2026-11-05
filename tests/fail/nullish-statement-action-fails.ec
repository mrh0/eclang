// error: The error from g(...) is ignored
fn g() throws String do
    throw "x"
fn f() throws String do
    throw "y"
fn main() do
    f() ?? g()
