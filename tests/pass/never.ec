use "core:c/stdio.ec"
use "core:errors.ec"

fn stop(code: Int): Never do
    panic("stopped with ${code}")

// A function ending in a call that never returns needs no `ret`.
fn pick(n: Int): Int do
    if n > 0 do
        ret n
    stop(n)

fn name(n: Int): CString do
    when n do
        1 do ret "one"c
        else do stop(n)

// After `if x == null do <never returns>`, x isn't null.
fn len(s: CString?): Int do
    if s == null do
        stop(0)
    ret strlen(s) as Int

fn orStop(s: CString?): CString = s ?? stop(1)

fn always(): Never = stop(2)

fn tail(v: Int?): Int do
    stop(3) if v == null
    ret v

declare fn strlen as (s: CString): Size

// C can't see that a call through a function value doesn't come back.
fn through(f: (Int) => Never): Int do
    f(1)

fn forever(): Never do
    while true do
        pass

fn lambda(on: Bool): Int do
    val f = fn (v: Int?) = v ?? panic("none")
    ret f(if on do 1 else null)

fn main(): Int do
    printf("%d %s %d %s %d\n"c, pick(4), name(1), len("abc"c), orStop("x"c), tail(5))
    val a: Int? = 7
    val b = if a != null do a else stop(8)
    val f: (Int) => Never = fn stop
    printf("%d %d\n"c, b, lambda(true))
    ret 0
