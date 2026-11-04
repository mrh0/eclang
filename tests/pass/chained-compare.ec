use "core:c/stdio.ec"

fn noisy(n: Int): Int do
    printf("noisy %d\n"c, n)
    ret n

fn inRange(x: Int, n: Int): Bool = 0 <= x < n

rec R as
    v: Int

fn b(x: Bool): Int = if x do 1 else 0

fn main(): Int do
    printf("%d %d %d\n"c, b(inRange(1, 3)), b(inRange(-1, 3)), b(inRange(3, 3)))
    printf("%d\n"c, b(1 < noisy(2) < 3))
    printf("%d\n"c, b(5 < noisy(2) < noisy(3)))
    printf("%d\n"c, b(1 < noisy(2) < noisy(3) <= 3))
    val a = 1
    val b = 2
    if a < b > 0 do
        printf("mixed\n"c)
    val r = R { 2 }
    printf("%d\n"c, b(0 < r.v < 3))
    var i = 0
    while 0 <= noisy(i) < 2 do
        i += 1
    if a > 5 do
        pass
    else if 0 < noisy(1) < 2 do
        printf("else if\n"c)
    printf("%d\n"c, b(1.5 < 2.5 < 3.5))
    val eq = (1 < 2) == (2 < 3)
    printf("%d\n"c, b(eq))
    ret 0
