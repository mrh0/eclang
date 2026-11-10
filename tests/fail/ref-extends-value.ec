// error: Counter is a `ref rec`, so a record that extends it must be one too: write `ref rec Sub extends Counter`.

ref rec Counter as
    count: Int = 0

rec Sub extends Counter as
    x: Int

fn main() do
    val s = Sub { 1, 2 }
