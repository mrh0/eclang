// error: a pointer to interface Named has no C type of its own.
inter Named as
    name: CString

rec Dog as
    name: CString

fn main(): Int do
    var d = Dog { name = "rex"c }
    val n: @Named = addrof(d)
    ret 0
