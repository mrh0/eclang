// error: `o.name` is the caller's memory and still points to memory of pool 'p'
rec Owner as
    name: String

fn fill(o: @Owner, c: Bool) do
    val p = newPool()
    defer freePool(p)
    if c do
        o.name = format(p, "hello ${1}")

fn main(): Int do
    ret 0
