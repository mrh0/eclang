use "core:io.ec"

mut rec Owner as
    name: String
    next: @Owner?

// A new value replaces what was in the pool.
fn replaced(o: @Owner) do
    val p = new Pool
    o.name = format(p, "scratch ${1}")
    io.log(o.name)
    o.name = "kept"
    destroy p

// A copy in a pool that lives on is fine, and so is the same pool freed later by the caller.
fn copied(o: @Owner) do
    val p = new Pool
    val s = format(p, "copied ${2}")
    o.name = clone(s)
    destroy p

fn inPool(o: @Owner, p: Pool) do
    o.name = format(p, "in the caller's pool ${3}")

// Assigning a whole value also replaces its fields.
fn whole(o: @Owner) do
    val p = new Pool
    o.name = format(p, "scratch ${4}")
    @o = Owner { name = "whole", next = null }
    destroy p

// A local record is no concern of the caller's.
fn local() do
    val p = new Pool
    var l = Owner { name = "", next = null }
    l.name = format(p, "local ${5}")
    io.log(l.name)
    destroy p

fn main(): Int do
    var o = Owner { name = "", next = null }
    replaced(addrof o)
    io.log(o.name)
    copied(addrof o)
    io.log(o.name)
    val p = new Pool
    inPool(addrof o, p)
    io.log(o.name)
    o.name = ""
    destroy p
    whole(addrof o)
    io.log(o.name)
    local()
    ret 0
