use "core:c/stdio.ec"

// A record and its first field are at one address, but a copy of one can't stand in for the other.

rec Inner as
    a: Int
    b: Int

rec Outer as
    first: Inner
    second: Inner

rec Holder as
    inner: @Inner
    outer: @Outer
    after: @Inner

rec Reversed as
    outer: @Outer
    inner: @Inner
    after: @Inner

// Pointers to one object stay shared, though their types differ.
rec Animal as
    legs: Int

rec Dog extends Animal as
    name: CString

rec Kennel as
    animal: @Animal
    dog: @Dog

type Meters = distinct Int

rec Road as
    plain: @Int
    length: @Meters

fn main(): Int do
    val o = new Outer { first = Inner { a = 1, b = 2 }, second = Inner { a = 3, b = 4 } }
    val z = new Inner { a = 5, b = 6 }

    val h = clone(Holder { inner = addrof o.first, outer = o, after = z })
    h.outer.second.b = 99
    h.inner.a = 70
    printf("%d %d %d %d %d\n"c, h.after.a, h.after.b, h.outer.second.b, h.outer.first.a, h.inner.a)

    val r = clone(Reversed { outer = o, inner = addrof o.first, after = z })
    r.outer.second.b = 98
    printf("%d %d %d %d\n"c, r.after.b, r.outer.second.b, r.inner.b, o.second.b)

    val d = new Dog { legs = 4, name = "Rex"c }
    val k = clone(Kennel { animal = d, dog = d })
    k.dog.legs = 3
    printf("%d %d %s\n"c, (k.dog !as @Animal) == k.animal, d.legs, k.dog.name)

    val n = new 5
    val road = clone(Road { plain = n, length = n !as @Meters })
    @road.plain = 6
    printf("%d %d\n"c, (@road.length) as Int, @n)
    ret 0
