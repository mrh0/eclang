// error: The lambda keeps `addrof x` in what 'l' points to, but `addrof x` points to 'x', a local variable
use "core:structures.ec"

fn run(f: () => None) do
    f()

fn fill(l: List<@Int>) do
    var x = 5
    run(fn () = l.push(addrof x))

fn main(): Int = 0
