// error: Argument 1 should be @Named: only a pointer to a record with the fields of Named fits @Named.
inter Named as
    name: CString

fn show(n: @Named): CString = n.name

fn main(): Int do
    var x = 1
    show(addrof(x))
    ret 0
