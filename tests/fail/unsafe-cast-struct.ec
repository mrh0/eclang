// error: Can't convert Point to Long with `!as`: it is a plain C cast, which only converts numbers and pointers

rec Point as
    x: Int
    y: Int

fn main(): Int do
    val p = Point { 1, 2 }
    val n = p !as Long
    ret 0
