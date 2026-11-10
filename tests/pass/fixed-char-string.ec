// A String value fills a `Char[N]` (checked to fit when the program runs), and `==` compares a `Char[N]`'s text with a String.
use "core:io.ec"

mut rec User as
    name: Char[8]
    tag: Char[4] = "abc"

type Code = distinct Char[4]

fn named(s: String): User = User { name = s }

fn label(c: Char[8]): String = "${c}"

fn main() do
    val s = "ann"
    var u = named(s)
    io.log(cstr(u.name))
    io.log(u.name == "ann")
    io.log("ann" == u.name)
    io.log(u.name == s)
    io.log(u.name != "an")
    io.log(u.name == "anne")
    u.name = concat("bo", "b")
    val bob = u.name == "bob"
    io.log("${u.name} ${bob}")
    val full: Char[8] = "12345678"
    io.log(full == "12345678")
    io.log(full == "1234567")
    val empty: Char[4] = ""
    io.log(empty == "")
    var t: Char[4] = s
    t = "${1 + 1}"
    io.log("${t} ${t.len}")
    io.log(label(s))
    val fixed = User { name = s[1..] }
    io.log(fixed.name == "nn")
    val c: Code = "ab" as Code
    io.log(c == "ab")
