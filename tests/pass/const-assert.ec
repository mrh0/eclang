// `const assert`: checked when compiling. Conditions on constants, `sizeof` (which C checks), types, and memory.
use "core:io.ec"

const SLOTS = 64
const assert SLOTS <= 255, "a slot index is a UChar"
const assert SLOTS % 8 == 0 and SLOTS > 0

rec Header as
    magic: UInt
    size: UInt
const assert sizeof Header == 8, "Header is written to disk as it is"
const assert offsetof(Header, size) == 4

rec Animal as
    legs: Int
rec Cat extends Animal as
    lives: Int
const assert Cat is Animal
const assert @Cat is @Animal
const assert Int fits Long
const assert Int is Int | String

inter Named as
    name: String
rec Person as
    name: String
    age: Int
const assert Person is Named

rec Node as
    value: Int

// The function keeps nothing of n.
fn value(n: @Node): Int do
    const assert not escapes(n)
    ret n.value

// `<T>` must have a name: checked for each type the function is compiled for.
fn greet(x: <T>): String do
    const assert <T> fits Named, "greet needs a name"
    ret "hello ${x.name}"

fn main(): Int do
    const assert sizeof Int == 4
    val p = new Pool
    defer destroy p
    val a = new Node { value = 1 } in p
    const assert a in p
    val b = new Node { value = 2 } in p
    const assert a outlives b
    val c = new Node { value = 3 }
    const assert c in pool
    pool do
        val d = new Node { value = 4 }
        const assert d in pool
        const assert c outlives d
    io.log(value(a) + value(b) + value(c))
    io.log(greet(Person { "ann", 30 }))
    ret 0
