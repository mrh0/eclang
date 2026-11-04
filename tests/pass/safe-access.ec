use "core:io.ec"

// `?.` reads a field of a value that may be null; the result is null when the value is.

rec Node as
    value: Int
    name: CString
    next: @Node?

fn show(n: Int?) do
    if n != null do
        io.log(n)
    else do
        io.log("null"c)

fn main(): Int do
    var last = Node { value = 3, name = "three"c, next = null }
    var first = Node { value = 1, name = "one"c, next = addrof(last) }
    val head: @Node? = addrof(first)
    val none: @Node? = null

    show(head?.value)
    show(none?.value)
    io.log(head?.name ?? "?"c)
    io.log(none?.name ?? "?"c)

    // Chains stop at the first null.
    show(head?.next?.value)
    show(head?.next?.next?.value)
    show(none?.next?.value)
    ret 0
