use "core:io.ec"

// A call's result points only into the arguments its function returns into, which the compiler finds from the body:
// find returns an entry's value, not its key, so the result outlives a key in a pool freed after the call.
rec Entry as
    key: String
    value: String

fn find(entries: Entry[], key: String): String do
    for e in entries do
        ret e.value if e.key == key
    ret ""

fn main(): Int do
    val entries = [Entry { key = "a1", value = "apple" }, Entry { key = "b", value = "banana" }]
    val p = new Pool
    val key = format(p, "a${1}")
    val found = find(entries, key)
    destroy p
    io.println(found)
    ret 0
