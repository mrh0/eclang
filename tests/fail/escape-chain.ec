// error: 'add' keeps 's' in what 'b' points to (it keeps it because it calls `put(...)` at line 12, which keeps its 's' in what its 'b' points to)
// An error about what a call keeps names the calls through which it keeps it.
use "core:io.ec"

rec Bag as
    items: String[]

fn put(b: @Bag, s: String) do
    b.items[0] = s

fn add(b: @Bag, s: String) do
    put(b, s)

fn main(): Int do
    var bag = Bag { items = new String[1] }
    pool do
        val s = "x${1}"
        add(addrof bag, s)
    io.println(bag.items[0])
    ret 0
