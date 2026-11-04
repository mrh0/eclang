use "core:c/stdio.ec"
use "core:errors.ec"

fn find(id: Int): CString? = if id == 1 do "one"c else null

fn parse(ok: Bool): Int throws Error do
    if ok do
        ret 5
    throw Error { 1, "bad" }

fn lookup(id: Int): Int do
    val u = find(id) ?? ret 0
    ret strlen(u) as Int

fn value(ok: Bool): Int do
    val n = parse(ok) ?? ret -1
    ret n * 2

fn narrowed(s: CString?): Int do
    val copy = s ?? ret 0
    // s is known not to be null from here on.
    ret (strlen(s) + strlen(copy)) as Int

fn deferred(id: Int) do
    defer printf("deferred\n"c)
    val u = find(id) ?? ret
    printf("found %s\n"c, u)

fn rethrow(id: Int): CString throws Error do
    val u = find(id) ?? throw Error { 2, "missing" }
    ret u

fn chain(a: CString?, b: CString?): CString do
    val v = a ?? b ?? ret "neither"c
    ret v

fn loops(values: Int[]): Int do
    var sum = 0
    for v in values do
        val s = find(v) ?? continue
        val t = (if v == 9 do null else s) ?? break
        sum += strlen(t) as Int
    ret sum

declare fn strlen as (s: CString): Size

fn main(): Int do
    printf("%d %d\n"c, lookup(1), lookup(2))
    printf("%d %d\n"c, value(true), value(false))
    printf("%d %d\n"c, narrowed("ab"c), narrowed(null))
    deferred(1)
    deferred(2)
    printf("%s\n"c, rethrow(1) ?? "failed"c)
    printf("%s\n"c, rethrow(2) ?? "failed"c)
    printf("%s %s %s\n"c, chain("a"c, null), chain(null, "b"c), chain(null, null))
    printf("%d\n"c, loops([1, 2, 1]))
    ret 0
