// error: A `debug requires` doesn't count
fn a(i: Int) debug requires i > 0 do
  ret
fn a(i: Int) do
  ret
fn main() do
  a(1)
