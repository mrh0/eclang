use "core:c/stdio.ec"

fn show(s: String) do
    printf("[%.*s]\n"c, s.len as Int, s.data)

fn word(): String do
    printf("once\n"c)
    ret "hello"

fn main(): Int do
    val s = "hello"
    show(s[1..])
    show(s[..<3])
    show(s[..=3])
    show(s[..])
    show(s[5..])
    show(s[..<0])
    val n = 2
    show(s[n + 1..])
    show(s[..<n + 1])
    show(word()[3..])
    val a = [1, 2, 3, 4]
    val b = a[2..]
    printf("%zu %d\n"c, b.len, b[0])
    val c = a[..<1]
    printf("%zu %d\n"c, c.len, c[0])
    ret 0
