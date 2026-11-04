// error: A value of type @Node is never null, so `?.` is not needed. Write `.value` instead.
rec Node as
    value: Int

fn main(): Int do
    var n = Node { value = 1 }
    val p = addrof(n)
    ret p?.value ?? 0
