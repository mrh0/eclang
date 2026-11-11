// error: Overloads told apart by `requires` must return the same type.
fn a(i: Int): Int requires i > 0 = 1
fn a(i: Int) do
  ret
fn main() do
  a(1)
