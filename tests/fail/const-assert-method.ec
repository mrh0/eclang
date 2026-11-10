// error: has no function `area` that interface Shape asks for.
inter Shape as
    fn area(): Double
rec Pt as
    x: Double
const assert Pt fits Shape
fn main(): Int do
    ret 0
