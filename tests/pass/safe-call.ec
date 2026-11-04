use "core:c/stdio.ec"

rec Queue as
    size: Int
    onDrop: (Int) => Int

fn drop(n: Int): Int = n + 1

fn count(q: @Queue, extra: Int = 0): Int = q.size + extra

fn text(q: @Queue): CString? = if q.size > 1 do "many"c else null

fn clear(q: @Queue) do
    printf("clear %d\n"c, q.size)

fn sideEffect(): Int do
    printf("evaluated\n"c)
    ret 1

fn maybe(on: Bool): @Queue? do
    printf("maybe\n"c)
    if on do
        ret new Queue { 4, fn drop }
    ret null

// Temporaries in deferred statements, at the end of a function and at an early exit.
fn deferred(leave: Bool): Int do
    defer maybe(false)?.clear()
    defer printf("%s\n"c, if 0 < sideEffect() < 2 do "in"c else "out"c)
    if leave do
        ret 1
    ret 2

fn main(): Int do
    val q: @Queue? = new Queue { 2, fn drop }
    val none: @Queue? = null
    printf("%d %d\n"c, q?.count() ?? -1, none?.count() ?? -1)
    printf("%d %d\n"c, q?.count(sideEffect()) ?? -1, none?.count(sideEffect()) ?? -1)
    printf("%d\n"c, q?.count(extra = 5) ?? -1)
    printf("%s %s\n"c, q?.text() ?? "none"c, none?.text() ?? "none"c)
    q?.clear()
    none?.clear()
    printf("%d\n"c, maybe(true)?.count() ?? -1)
    maybe(false)?.clear()
    printf("%d\n"c, q?.onDrop(1) ?? -1)
    var i = 0
    while maybe(i < 1)?.count() != null do
        i += 1
    printf("%d\n"c, i)
    printf("%d %d\n"c, deferred(true), deferred(false))
    ret 0
