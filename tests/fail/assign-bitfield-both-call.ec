// error: The target and the value both call something
rec Flags as
    a: Int : 3
    b: Int : 5

var f: Flags = Flags { 1, 2 }

fn get(): @Flags = addrof f

fn one(): Int = 1

fn main() do
    get().a = one()
