use "core:c/stdio.ec"
use "core:errors.ec"

fn connect(host: CString, port: Int = 80, timeout: Int = 30, retries: Int = 3): Int do
    printf("%s %d %d %d\n"c, host, port, timeout, retries)
    ret port + timeout

fn both(a: Int, b: CString): Int do
    printf("%d %s\n"c, a, b)
    ret a

fn first(a: Int = 1, b: Int = 2): Int = a * 10 + b

fn picks(x: Int | CString, y: Int = 0): Int do
    if x is Int do
        ret x + y
    ret y

fn gen(a: <T>, tag: Int = 0): <T> do
    printf("%d\n"c, tag)
    ret a

fn take(f: (Int) => Int, x: Int = 0): Int = f(x)

rec Client as
    id: Int

fn send(c: Client, data: Int, flags: Int = 0): Int = c.id + data + flags

fn keyword(int: Int = 1, double: Int = 2): Int = int * 10 + double

fn main(): Int do
    printf("%d\n"c, keyword(double = 5))
    connect("a"c)
    connect("b"c, 1)
    connect("c"c, timeout = 5)
    connect("d"c, retries = 9, port = 7)
    connect(host = "e"c, timeout = 1, retries = 2, port = 3)
    connect("f"c, 2, retries = 4)
    both(b = "x"c, a = 1)
    printf("%d %d %d %d\n"c, first(), first(7), first(b = 7), first(b = 3, a = 4))
    printf("%d %d\n"c, picks(1, y = 2), picks("s"c, y = 3))
    printf("%d\n"c, gen(5, tag = 6))
    printf("%d\n"c, take(x = 4, f = fn (v: Int) = v * 2))
    val c = Client { 100 }
    printf("%d %d\n"c, c.send(1, flags = 2), c.send(data = 3))
    printf("%d\n"c, 8 |> first(b = 1))
    ret 0
