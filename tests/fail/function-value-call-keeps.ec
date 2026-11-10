// error: 'f' is a function value of type (List<String>, String) => None, and `fn add` (line 9), one of that type, keeps argument 2 in what argument 1 points to, but here argument 2 points to memory from the pool of the `pool` block
use "core:structures.ec"

fn add(l: List<String>, s: String) do
    l.push(s)

fn main(): Int do
    val names: List<String> = list()
    val f: (List<String>, String) => None = fn add
    pool do
        f(names, "n ${1}")
    ret 0
