// error: Can't use 'found' here: it points to memory of pool 'p' that `destroy p` at line 19 freed (it came out of `find(...)`, and the result of `find` may point into what its parameter 'key' was given
// find returns its key here, so its result is in p.
use "core:io.ec"

rec Entry as
    key: String
    value: String

fn find(entries: Entry[], key: String): String do
    for e in entries do
        ret key if e.key == key
    ret ""

fn main(): Int do
    val entries = [Entry { key = "a1", value = "apple" }, Entry { key = "b", value = "banana" }]
    val p = new Pool
    val key = format(p, "a${1}")
    val found = find(entries, key)
    destroy p
    io.println(found)
    ret 0
