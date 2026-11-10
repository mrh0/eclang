// `packed`, `align(n)`, bit-fields and unions: the C layout of a record.
use "core:io.ec"

const WIDE = 32

mut packed rec Header as
    tag: UChar
    size: Int
    words: UShort[3]
    name: Char[3]

mut rec Plain as
    tag: UChar
    size: Int

mut packed rec Outer as
    tag: UChar
    inner: Plain

align(WIDE) rec Line as
    x: Int

packed align(4) rec Both as
    a: UChar
    b: Int

rec Holder as
    line: Line

mut rec Flags as
    a: UInt : 3
    b: Int : 3
    on: Bool : 1
    rest: UInt:9 = 5

packed rec Bits<T> as
    tag: T
    flag: UChar : 1

mut union Value as
    i: Int
    f: Float
    bytes: UChar[4]

packed union Mixed as
    big: Long
    small: UChar

align(16) union Aligned as
    a: Int

mut rec Tagged as
    kind: Int
    value: Value

fn half(h: @Header) do
    h.size /= 2

fn main(): Int do
    // packed
    io.log("${sizeof Header} ${alignof Header} ${offsetof(Header, size)} ${offsetof(Header, words)} ${offsetof(Header, name)}")
    var h = Header { 1, 200, [1, 2, 3], ['a', 'b', 0 as Char] }
    h.words[1] = 20
    h.size += 2
    half(addrof h)
    val words = h.words
    h.words = [7, 8, 9]
    val same = h
    io.log("${h}")
    io.log("${words} ${h == same} ${hash(h) == hash(same)}")
    for w in h.words do
        io.log(w)
    io.log(cstr(h.name))
    val tagAddress = addrof h.tag
    io.log(@tagAddress)
    io.log("${sizeof Outer} ${offsetof(Outer, inner)}")
    var o = Outer { 1, Plain { 2, 3 } }
    o.inner.size = 4
    io.log("${o}")

    // align(n), also for what a pool allocates
    io.log("${sizeof Line} ${alignof Line} ${alignof Holder} ${alignof Both} ${sizeof Both}")
    val line = new Line { 1 }
    val lines = new Line[3]
    val copy = clone(line)
    val holder = new Holder { Line { 2 } }
    io.log("${(line !as Size) % 32} ${(lines.data !as Size) % 32} ${(copy !as Size) % 32} ${(holder !as Size) % 32}")

    // bit-fields
    var f = Flags { 1, -1, true }
    io.log("${f} ${sizeof Flags}")
    f.a += 1
    f.a /= 2
    f.rest = 511
    f.b = -4
    f.on = false
    io.log("${f}")
    val g = f
    io.log("${f == g} ${hash(f) == hash(g)}")
    val p = addrof f
    p.a = 3
    p.a <<= 1
    io.log(f.a)
    val bits = Bits<Int> { 1, 1 }
    io.log("${bits} ${sizeof Bits<Int>}")

    // unions
    var v = Value { i = 0x3f800000 }
    io.log("${v.f} ${v.bytes[3]} ${sizeof Value}")
    v.f = 2.0
    io.log(v.i)
    v.bytes[3] = 0
    io.log(v.i)
    io.log("${v}")
    val first = Value { 1 }
    io.log(first.i)
    val m = Mixed { small = 9 }
    io.log("${sizeof Mixed} ${alignof Mixed} ${m.small}")
    io.log("${alignof Aligned} ${sizeof Aligned}")
    var t = Tagged { 1, Value { f = 1.5 } }
    t.value.i = 3
    val shared = clone(new Tagged { 2, t.value })
    io.log("${@shared} ${shared.value.i}")
    ret 0
