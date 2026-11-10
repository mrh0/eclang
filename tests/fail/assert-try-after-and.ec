// error: `try` can't be used after `and` in `assert`
use "core:errors.ec"
fn parse(s: String): Int throws Error do
    throw Error { 1, "no" } if s.len == 0
    ret 1
fn check(s: String) throws Error do
    assert s.len > 0 and try parse(s) > 0
fn main(): Int do
    ret 0
