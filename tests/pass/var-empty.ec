use "core:lib.ec"

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
    log(count)
    log(done)
    log(ratio)
    log(text.len)
    log(items.len)
    log(name == null)
    log(p.x + p.y)
    fill(addrof(p))
    log(p.x + p.y)
    count += 2
    log(count)
    // A record in a hierarchy still knows its type.
    var sq: Square
    sq.w = 5
    log(area(addrof(sq)))
    ret 0
