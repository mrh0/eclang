use "core:c/stdio.ec"

// Used before it is defined, in either order.
type Both = Small | Text
type Small = Int
type Text = CString

type UserId = distinct Int
type Meters = distinct Double
type Name = distinct String
type Mask = distinct UInt
type Ids = UserId[]

rec User as
    id: UserId
    name: Name

fn describe(v: Both): CString = when v do
    is Int do "number"c
    is CString do "text"c

fn show(id: UserId): CString = "user id"c
fn show(n: Int): CString = "int"c

fn next(id: UserId): UserId = id + 1

fn b(x: Bool): Int = if x do 1 else 0

fn main(): Int do
    printf("%s %s\n"c, describe(1), describe("a"c))
    val id: UserId = 5
    val other = 7 as UserId
    printf("%s %s\n"c, show(id), show(5))
    printf("%d %d\n"c, next(id) as Int, (id + other) as Int)
    printf("%d %d %d\n"c, b(id < other), b(id == 5), b(id == other))
    val m: Meters = 1.5
    printf("%.2f\n"c, (m * 2.0) as Double)
    val u = User { 1, "ann" as Name }
    val copy = clone(u)
    val line = "${copy.id} ${copy.name} ${copy.name.len} ${copy.name[0]}"
    printf("%.*s\n"c, line.len as Int, line.data)
    printf("%d\n"c, b(u.name == copy.name))
    val ids: Ids = [id, other]
    for v in ids do
        printf("%d\n"c, v as Int)
    var n: UserId = 1
    n += 2
    n = n * 3
    printf("%d\n"c, n as Int)
    val mask: Mask = 12
    printf("%u %u\n"c, (mask >> 2) as UInt, (mask & (4 as Mask)) as UInt)
    val q: UserId = 10
    printf("%d %d\n"c, (q / 2) as Int, (-q) as Int)
    val maybe: UserId? = id
    printf("%d\n"c, (maybe ?? 0) as Int)
    val word = "hi" as Name
    for ch in word do
        printf("%c\n"c, ch)
    when id do
        5 do printf("five\n"c)
        else do printf("not\n"c)
    ret 0
