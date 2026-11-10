// error: Sub is a `ref rec`, but Base isn't

rec Base as
    x: Int

ref rec Sub extends Base as
    y: Int

fn main() do
    val s = new Sub { 1, 2 }
