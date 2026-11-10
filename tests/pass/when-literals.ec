use "core:c/stdio.ec"

// Literals in the arms of a `when` used as a value live as long as the statement's block, though the arms are C blocks.

rec Box as
    items: Int[]

rec Point as
    x: Int
    y: Int

fn sum(a: Int[]): Int do
    var s = 0
    for x in a do
        s = s + x
    ret s

fn id(a: Int[]): Int[] = a

fn plain(n: Int): Int do
    val a: Int[] = when n do
        1 do [10, 20, 30]
        else do [1]
    val b: Int[] = when n do
        1 do [1, 2, 3]
        else do [2]
    ret sum(a) * 100 + sum(b)

fn inRecord(n: Int): Int do
    val b = when n do
        1 do Box { items = [4, 5, 6] }
        else do Box { items = [7] }
    val c = when n do
        1 do Box { items = [40, 50, 60] }
        else do Box { items = [70] }
    ret sum(b.items) * 1000 + sum(c.items)

fn nestedArrays(n: Int): Int do
    val a: Int[][] = when n do
        1 do [[1, 2], [3, 4]]
        else do [[5]]
    val b: Int[][] = when n do
        1 do [[10, 20], [30, 40]]
        else do [[50]]
    ret sum(a[1]) * 1000 + sum(b[1])

fn nestedWhen(n: Int, m: Int): Int do
    val a: Int[] = when n do
        1 do when m do
            2 do [1, 2]
            else do [3]
        else do [4]
    val b: Int[] = when n do
        1 do when m do
            2 do [10, 20]
            else do [30]
        else do [40]
    ret sum(a) * 100 + sum(b)

fn throughCall(n: Int): Int do
    val a = when n do
        1 do id([1, 2, 3])
        else do id([4])
    val b = when n do
        1 do id([10, 20, 30])
        else do id([40])
    ret sum(a) * 100 + sum(b)

fn cArray(n: Int): Int do
    val a = when n do
        1 do [1, 2, 3]c
        else do [4]c
    val b = when n do
        1 do [10, 20, 30]c
        else do [40]c
    ret unsafe (a[2] * 100 + b[1])

fn records(n: Int): Int do
    val a: Point[] = when n do
        1 do [Point { x = 1, y = 2 }, Point { x = 3, y = 4 }]
        else do [Point { x = 5, y = 6 }]
    val b: Point[] = when n do
        1 do [Point { x = 10, y = 20 }, Point { x = 30, y = 40 }]
        else do [Point { x = 50, y = 60 }]
    ret a[1].y * 100 + b[1].y

fn pointers(n: Int): Int do
    val p: @Point = when n do
        1 do addrof Point { x = 1, y = 2 }
        else do addrof Point { x = 3, y = 4 }
    val q: @Point = when n do
        1 do addrof Point { x = 10, y = 20 }
        else do addrof Point { x = 30, y = 40 }
    ret p.y * 100 + q.y

fn rounds(): Int do
    var total = 0
    for i in 0..<3 do
        val a: Int[] = when i do
            0 do [1, 1]
            1 do [i, 2, 3]
            else do [4]
        total = total * 10 + sum(a)
    ret total

fn main(): Int do
    printf("%d %d %d\n"c, plain(1), plain(2), inRecord(1))
    printf("%d %d %d\n"c, nestedArrays(1), nestedWhen(1, 2), nestedWhen(2, 0))
    printf("%d %d %d\n"c, throughCall(1), cArray(1), records(1))
    printf("%d %d\n"c, pointers(1), rounds())
    ret 0
