// error: `o.name` is the caller's memory and still points to memory of pool 'p'
rec Owner as
    name: String

fn fill(o: @Owner) do
    val p = newPool()
    o.name = format(p, "hello ${1}")
    freePool(p)

fn main(): Int do
    var o = Owner { name = "" }
    fill(addrof o)
    ret 0
