// error: This lambda keeps its parameter 's' in what 'out' points to
use "core:structures.ec"

fn each(values: String[], f: (String) => None) do
    for v in values do
        f(v)

fn main(): Int do
    val out: List<String> = list()
    pool do
        each(["a ${1}"], fn (s) = out.push(s))
    ret 0
