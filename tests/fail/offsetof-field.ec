// error: P has no field 'z'.
rec P as
    x: Int

fn main(): Int do
    val a = offsetof(P, z)
    ret 0
