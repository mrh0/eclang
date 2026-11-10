// error: 'fill' keeps 'value' in what 'm' points to
// The same through a variable holding what the call returned.
use "core:io.ec"

mut rec Entry as
    key: Int
    value: String

rec Table as
    entries: Entry[]

fn find(t: @Table, key: Int): @Entry = addrof t.entries[key]

fn fill(m: @Table, value: String) do
    val e = find(m, 0)
    e.value = value

fn main() do
    val t = new Table { [Entry { 0, "" }] }
    pool do
        fill(t, "${1}")
    io.log(t.entries[0].value)
