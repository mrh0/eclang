use "core:lib.ec"

// `==` on records compares field by field; a `@Interface` parameter accepts a pointer to any record that fits.
rec Point as
    x: Int
    y: Int

rec Tagged as
    name: CString
    label: String
    at: Point
    next: @Point?

inter Named as
    name: CString

rec Dog as
    name: CString
    age: Int

fn rename(n: @Named, name: CString) do
    n.name = name

fn show(n: @Named) do
    log(n.name)

fn main(): Int do
    val a = Point { x = 1, y = 2 }
    var b = Point { x = 1, y = 2 }
    log(a == b)
    b.y = 3
    log(a == b)
    log(a != b)

    val t1 = Tagged { name = "a"c, label = "l", at = a, next = null }
    val t2 = Tagged { name = "a"c, label = "l", at = a, next = null }
    log(t1 == t2)
    val t3 = Tagged { name = "a"c, label = "m", at = a, next = null }
    log(t1 == t3)

    var dog = Dog { name = "rex"c, age = 3 }
    rename(addrof(dog), "fido"c)
    show(addrof(dog))
    var tagged = t1
    show(addrof(tagged))
    ret 0
