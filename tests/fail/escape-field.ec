// error: 'rename' allocates in the current pool and keeps that memory in what 'r' points to
use "core:io.ec"
use "core:structures.ec"

mut rec R as
    name: String
fn rename(r: @R, n: Int) do
    r.name = "${n}"
fn main() do
    val r = new R { "" }
    pool do
        rename(r, 3)
    io.log(r.name)
