use "core:lib.ec"

// `...rest: T` takes any number of T arguments; inside the function rest is a T[].
fn sum(...rest: Int): Int do
    var total = 0
    for n in rest do
        total = total + n
    ret total

fn greet(greeting: CString, ...names: CString) do
    for name in names do
        log(greeting)
        log(name)

fn main(): Int do
    log(sum(1, 2, 3))
    log(sum())
    greet("hi"c, "Ada"c, "Bob"c)
    greet("nobody"c)
    ret 0
