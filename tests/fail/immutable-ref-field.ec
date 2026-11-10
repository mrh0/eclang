// error: Counter isn't a `mut ref rec`: its fields are fixed once it is made. Make a new one with the change instead, `new Counter { ...@old, count = value }`, or declare `mut ref rec Counter`
ref rec Counter as
    count: Int

fn bump(c: Counter) do
    c.count += 1

fn main() do
    bump(new Counter { 0 })
