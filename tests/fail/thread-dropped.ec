// error: This Thread is dropped, but Thread is a `resource`
use "core:thread.ec"

fn idle() do
    ret

fn main(): Int throws Error do
    try thread.spawn(fn idle)
    ret 0
