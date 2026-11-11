// error: Two functions with the same parameter types can only both be defined when `requires` tells them apart
fn a(i: Int) do
  ret
fn a(i: Int) do
  ret
fn main() do
  a(1)
