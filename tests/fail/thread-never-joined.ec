// error: 'worker' is never used, but Thread is a `resource`: each one has to be passed on, as it holds something that must be ended. Join it
use "core:thread.ec"

fn idle() do
    ret

fn main(): Int throws Error do
    val worker = try thread.spawn(fn idle)
    ret 0
