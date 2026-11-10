// error: Can't store this in field 'next': it points to 'other', a local variable, which is freed when its block ends, but field 'next' outlives this function
mut rec Node as
    next: @Node?

fn link(n: @Node) do
    var other = Node { next = null }
    n.next = addrof other

fn main(): Int = 0
