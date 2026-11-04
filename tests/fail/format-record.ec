// error: Can't put a Point? into a String with `${...}`. It may be null: check it first, or write `${x ?? default}`.
rec Point as
    x: Int
    y: Int

fn find(found: Bool): Point? do
    ret null if !found
    ret Point { 1, 2 }

fn main(): Int do
    val p = find(true)
    val s = "p is ${p}"
    ret 0
