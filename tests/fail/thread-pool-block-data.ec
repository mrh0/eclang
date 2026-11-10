// error: but here 'data' points to memory from the pool of the `pool` block
use "core:thread.ec"

fn work(n: @Int) do
    @n = 5

fn main(): Int throws Error do
    pool do
        val n = new 0
        val t = try thread.spawn(fn work, n)
        try thread.detach(t)
    ret 0
