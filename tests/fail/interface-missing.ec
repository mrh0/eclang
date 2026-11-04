// error: Rock is missing `name: CString`, required by Named
inter Named as
    name: CString

rec Rock as
    weight: Int

fn greet(who: Named) do
    pass

fn main(): Int do
    greet(Rock { 5 })
    ret 0
