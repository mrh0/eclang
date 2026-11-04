// error: or make the elements nullable: `new (@Point?)[n]`.
rec Point as
    x: Int

fn main(): Int do
    val a = new @Point[2]
    ret 0
