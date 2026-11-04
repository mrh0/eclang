// panic: panic: no value
use "core:errors.ec"

fn orDie(v: Int?): Int = v ?? panic("no value")

fn main(): Int do
    ret orDie(null)
