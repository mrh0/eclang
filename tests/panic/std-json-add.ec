// panic: json.add needs an array
use "core:json.ec"

fn main(): Int do
    json.add(json.object(), 1)
    ret 0
