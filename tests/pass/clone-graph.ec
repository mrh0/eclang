use "core:io.ec"

// `clone` copies each pointer once: a cycle is copied as a cycle, and a shared value stays shared.

mut rec Node as
    v: Int
    next: @Node?

fn main(): Int do
    val a = new Node { v = 1, next = null }
    val b = new Node { v = 2, next = a }
    a.next = b
    val c = clone(a)
    c.v = 10
    io.log(c.v)
    io.log(a.v)
    io.log(c.next!.v)
    io.log(c.next!.next!.v)
    val shared = new Node { v = 7, next = null }
    val copy = clone([shared, shared])
    copy[0].v = 8
    io.log(copy[1].v)
    io.log(shared.v)
    ret 0
