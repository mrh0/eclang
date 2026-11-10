// error: 'keepIn' keeps 's' in what 'l' points to
use "core:structures.ec"

fn keepIn(l: List<String>, s: String) do
    l.push(s)

fn main(): Int do
    val names: List<String> = list()
    for i in 0..<3 do
        pool do
            keepIn(names, "${i}")
    ret names.len as Int
