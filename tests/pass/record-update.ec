use "core:c/stdio.ec"

rec Point as
    x: Int
    y: Int
    z: Int = 9

rec Named extends Point as
    name: CString

rec Box<T> as
    value: <T>
    size: Int

fn shifted(p: Point, by: Int): Point = Point { ...p, x = p.x + by }

fn made(): Point do
    printf("made\n"c)
    ret Point { 1, 2, 3 }

fn show(p: Point) do
    printf("%d %d %d\n"c, p.x, p.y, p.z)

fn main(): Int do
    val p = Point { 1, 2 }
    show(Point { ...p, x = 5 })
    show(Point { ...p })
    val q: Point = { ...p, y = 7, z = 8 }
    show(q)
    show(shifted(p, 10))
    show(Point { ...made(), y = 0 })
    val n = Named { 1, 2, 3, "n"c }
    val m = Named { ...n, name = "m"c, y = 5 }
    printf("%s %d\n"c, m.name, m.y)
    show(Point { ...n, x = 4 })
    val b = Box<Int> { 1, 2 }
    val c = Box { ...b, value = 3 }
    printf("%d %d\n"c, c.value, c.size)
    val r = addrof q
    show({ ...@r, x = 0 })
    var i = 0
    while Point { ...made(), x = i }.x < 1 do
        i += 1
    show(Point {
        ...p,
        x = 6
    })
    ret 0
