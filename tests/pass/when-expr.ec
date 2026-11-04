use "core:c/stdio.ec"
use "core:errors.ec"

rec Circle as
    r: Int

rec Square as
    side: Int

type Shape = Circle | Square

fn area(s: Shape): Int = when s do
    is Circle do s.r * s.r * 3
    is Square do s.side * s.side

fn word(n: Int): CString do
    val w = when n do
        1 do "one"c
        2, 3 do "few"c
        else do "many"c
    ret w

fn early(n: Int): Int do
    val half = when n % 2 do
        0 do n / 2
        else do ret -1
    ret half + 100

fn loud(n: Int): Int do
    val v: Long = when n do
        0 do 5
        else do
            ret 9
    ret v as Int

fn nullable(n: Int): Int? do
    ret when n do
        0 do null
        else do n

fn calc(): Int throws Error do
    ret 11

fn safe(n: Int): Int throws Error do
    val v = when n do
        0 do try calc()
        else do n
    ret v

fn nested(a: Int, b: Int): Int do
    val x = when a do
        0 do when b do
            0 do 1
            else do 2
        else do 3
    ret x

fn sum(values: Int[]): Int do
    var total = 0
    for v in values do
        total += when v do
            0 do continue
            9 do break
            else do v
    ret total

fn twice(v: <T>, n: Int): <T> = when n do
    0 do v
    else do twice(v, n - 1)

val pick = fn (x: Int) = (when x do
    1 do "lambda"c
    else do "other"c
)

fn main(): Int do
    printf("%d %d\n"c, area(Circle { 2 }), area(Square { 3 }))
    printf("%s %s %s\n"c, word(1), word(3), word(9))
    printf("%d %d\n"c, early(4), early(5))
    printf("%d %d\n"c, loud(0), loud(1))
    printf("%d %d\n"c, nullable(0) ?? -1, nullable(4) ?? -1)
    printf("%d %d\n"c, safe(0) ?? -1, safe(4) ?? -1)
    printf("%d %d %d\n"c, nested(0, 0), nested(0, 1), nested(1, 1))
    var total = 0
    for i in 0..<4 do
        total += when val m = i * 2 do
            2 do m + 10
            else do m
    printf("%d\n"c, total)
    val b = true
    val t = when b do
        true do "yes"c
        false do "no"c
    printf("%s\n"c, t)
    printf("%s\n"c, (when b do
        true do "paren"c
        false do "x"c
    ))
    printf("%d %d %s %s\n"c, sum([1, 0, 2, 9, 4]), twice(5, 2), twice("t"c, 1), pick(1))
    ret 0
