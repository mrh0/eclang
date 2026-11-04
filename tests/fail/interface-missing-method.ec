// error: Argument 1 should be Shape, which asks for `fn area(): Double`, but there is no function 'area' for a Tri. Define one: `fn area(x: Tri): Double`.
use "core:io.ec"

inter Shape as
    fn area(): Double

rec Tri as
    b: Double

fn report(s: Shape) do
    io.log(s.area())

fn main(): Int do
    report(Tri { 1.0 })
    ret 0
