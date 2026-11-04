use "core:io.ec"

// `...rest: T` takes any number of T arguments; inside the function rest is a T[].
fn sum(...rest: Int): Int do
    var total = 0
    for n in rest do
        total = total + n
    ret total

fn greet(greeting: CString, ...names: CString) do
    for name in names do
        io.log(greeting)
        io.log(name)

fn main(): Int do
    io.log(sum(1, 2, 3))
    io.log(sum())
    greet("hi"c, "Ada"c, "Bob"c)
    greet("nobody"c)
    ret 0
