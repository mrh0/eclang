use "core:io.ec"
use "core:structures.ec"

// SlotPool: values whose lifetimes don't nest, reached by keys rather than pointers. A key of a value taken away finds nothing,
// even after its slot holds another value.

rec Node as
    name: String
    next: SlotKey?

fn main(): Int do
    val nodes: SlotPool<Node> = slotPool()
    val a = nodes.add(Node { "a", null })
    val b = nodes.add(Node { "b", a })
    val c = nodes.add(Node { "c", b })
    io.log(nodes.size())
    // Follow the links from c.
    var at: SlotKey? = c
    while at != null do
        val node = nodes.get(at) ?? panic("a link to nothing")
        io.log(node.name)
        at = node.next

    io.log(nodes.remove(b))
    io.log(nodes.remove(b))
    io.log(nodes.get(b) == null)
    io.log(nodes.has(b))
    // b's slot is used again, but b's key doesn't find the new value.
    val d = nodes.add(Node { "d", null })
    io.log(d.index == b.index)
    io.log(nodes.get(b) == null)
    io.log((nodes.get(d) ?? panic("d is there")).name)
    io.log(nodes.set(b, Node { "stale", null }))
    io.log(nodes.set(d, Node { "d2", null }))
    io.log(nodes.size())

    for (key, node) in nodes do
        io.log("${key.index}: ${node.name}")

    // A stale link is found, not followed into another value.
    val link = nodes.get(c) ?? panic("c is there")
    io.log(link.next != null and nodes.get(link.next!) == null)
    ret 0
