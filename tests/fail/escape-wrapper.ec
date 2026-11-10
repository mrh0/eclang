// error: 'add' keeps 's' in what 'l' points to
use "core:io.ec"
use "core:structures.ec"

fn add(l: List<String>, s: String) do
    l.push(s)
fn main() do
    val l: List<String> = list()
    pool do
        add(l, "${1}")
