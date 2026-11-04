use "core:io.ec"

// `var x: T` without a value starts out empty.
rec Point as
    x: Int
    y: Int

rec Shape as
    w: Int

rec Square extends Shape as
    side: Int

fn fill(p: @Point) do
    p.x = 3
    p.y = 4

fn area(s: @Shape): Int = s.w * s.w

fn main(): Int do
    var count: Int
    var done: Bool
    var ratio: Double
    var text: String
    var items: Int[]
    var name: CString?
    var p: Point
    io.log(count)
    io.log(done)
    io.log(ratio)
    io.log(text.len)
    io.log(items.len)
    io.log(name == null)
    io.log(p.x + p.y)
    fill(addrof(p))
    io.log(p.x + p.y)
    count += 2
    io.log(count)
    // A record in a hierarchy still knows its type.
    var sq: Square
    sq.w = 5
    io.log(area(addrof(sq)))
    ret 0
