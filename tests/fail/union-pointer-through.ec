// error: Reading '.p' of union Slot isn't checked
union Slot as
    n: Long
    p: @Int

fn main() do
    var x = 1
    var s = Slot { p = addrof x }
    @s.p = 2
