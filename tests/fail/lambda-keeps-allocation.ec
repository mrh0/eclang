// error: This lambda allocates in the current pool and keeps that memory in what 'out' points to
use "core:structures.ec"

fn each(values: Int[], f: (Int) => None) do
    for v in values do
        f(v)

fn main(): Int do
    val out: List<String> = list()
    each([1, 2], fn (i) = out.push("${i}"))
    ret 0
