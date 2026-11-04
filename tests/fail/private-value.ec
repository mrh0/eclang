// error: 'NONE' is private to module 'geo' (`priv`)
use "core:io.ec"
use "../pass/modules/geo.ec"

fn main(): Int do
    io.log(geo.NONE)
    ret 0
