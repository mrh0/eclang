// A library: only what is exported is visible to C.
module "geo"

use "core:io.ec"

packed rec Header as
    tag: UChar
    size: Int

rec Flags as
    a: UInt : 3
    b: Int : 3

union Value as
    i: Int
    f: Float

rec Point as
    x: Int
    y: Int
    name: Char[8]

var calls = 0
val origin = Point { 0, 0, ['o', 0 as Char, 0 as Char, 0 as Char, 0 as Char, 0 as Char, 0 as Char, 0 as Char] }

fn hidden(a: Int): Int = a * 2

/** The Manhattan distance to the origin. */
export fn distance(p: Point): Int do
    calls += 1
    ret hidden(p.x) / 2 + hidden(p.y) / 2

export extern "geo_calls" fn callCount(): Int = calls

export fn originName(): CString = origin.name.data

export fn greet(name: CString): CString do
    ret "hello ${name}".cstr()

export fn header(size: Int): Header = Header { 7, size }

export fn flags(): Flags = Flags { 5, -2 }

export fn value(f: Float): Value = Value { f = f }

export fn floatBits(f: Float): Int = value(f).i

export fn apply(f: (Int) => Int, x: Int): Int = f(x) + 1

export fn safeDivide(a: Int, b: Int): Int throws CString do
    throw "division by zero"c if b == 0
    ret a / b
