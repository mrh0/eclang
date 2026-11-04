// error: Read the record first: `...@p`
rec Point as
    x: Int

fn main(): Int do
    val p = new Point { 1 }
    val q = Point { ...p, x = 2 }
    ret q.x
