// error: Type Delta is private to module 'geo' (`priv`)
use "../pass/modules/geo.ec"

fn main(): Int do
    val d = geo.Delta { 1, 2 }
    ret 0
