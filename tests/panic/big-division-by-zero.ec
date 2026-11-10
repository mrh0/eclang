// panic: division by zero
use "core:big.ec"

fn main(): Int do
    val q = big.integer(1).div(big.zero)
    ret q.sign()
