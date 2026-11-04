// error: Can't return this: it points to 'n', a local variable
rec Box as
    item: @Int

fn make(): Box do
    var n = 1
    ret Box { item = addrof n }

fn main(): Int = 0
