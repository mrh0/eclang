// error: fn tell(x: @F): Long throws Error
use "core:errors.ec"
inter T as
    fn tell(): Long throws Error
rec F as
    x: Int
fn tell(f: @F): Long = 1
fn show(t: @T) do
    pass
fn main() do
    show(new F { x = 1 })
