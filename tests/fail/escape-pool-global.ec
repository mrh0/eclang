// error: 'keep' allocates in the current pool and keeps that memory in a global
use "core:io.ec"
use "core:structures.ec"

var g: @Int? = null
fn keep() do
    g = new 1
fn main() do
    pool do
        keep()
