// error: The type that 'reset' (
rec Tally as
    count: Int

fn reset(mut v: <mut T>) do
    v = v

fn main() do
    var t = Tally { 3 }
    reset(mut t)
