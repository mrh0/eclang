// error: its field `name` has no empty value (a CString can't be null
rec Person as
    name: CString
    age: Int

fn main(): Int do
    var p: Person
    ret p.age
