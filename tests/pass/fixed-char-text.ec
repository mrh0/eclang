// A String literal fills a `Char[N]`, as in C: the rest is zeros, and a literal of exactly N bytes has no 0 at the end.
use "core:io.ec"

mut rec User as
    name: Char[8]
    tag: Char[4] = "abc"

var greeting: Char[6] = "hello"

fn show(c: Char[8]) do
    io.log(cstr(c))

fn main() do
    var a: Char[8] = "abc"
    io.log(cstr(a))
    a = "defg"
    io.log(cstr(a))
    val full: Char[3] = "xyz"
    io.log(cstr(full))
    val u = User { name = "ann" }
    io.log("${cstr(u.name)} ${cstr(u.tag)} ${u.tag.len}")
    var v = u
    v.name = "bob\n"
    io.log(cstr(v.name))
    io.log(cstr(greeting))
    show("q")
    val s = "plain"
    io.log(s.len)
    val m: String | Char[4] = "ab"
    io.log(m is String)
