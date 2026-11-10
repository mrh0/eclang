// error: Reading '.p' of union Slot isn't checked
union Slot as
    n: Long
    p: @Int

fn main() do
    var x = 1
    var s = Slot { n = 7 }
    s.p = addrof x
    val q = s.p
