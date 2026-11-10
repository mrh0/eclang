// error: here it points to memory of pool 'scratch', which this function made
use "core:thread.ec"

fn work(n: @Int) do
    @n = 5

fn main(): Int throws Error do
    val scratch = new Pool
    val n = new 0 in scratch
    val t = try thread.spawn(fn work, n)
    try thread.detach(t)
    destroy scratch
    ret 0
