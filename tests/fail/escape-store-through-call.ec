// error: 'fill' keeps 'value' in what 'm' points to, but here 'value' points to memory from the pool of the `pool` block
// A store through what a call returns may write into what its arguments point to: find(m, k) may be in m.
use "core:io.ec"

mut rec Entry as
    key: Int
    value: String

rec Table as
    entries: Entry[]

fn find(t: @Table, key: Int): @Entry = addrof t.entries[key]

fn fill(m: @Table, value: String) do
    find(m, 0).value = value

fn main() do
    val t = new Table { [Entry { 0, "" }] }
    pool do
        fill(t, "${1}")
    io.log(t.entries[0].value)
