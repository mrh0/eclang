use "core:io.ec"
use "core:errors.ec"

inter Teller as
    fn tell(): Long throws Error
    fn write(String) throws Error
    fn flush(): None throws Error

mut rec F as
    pos: Long

fn tell(f: @F): Long throws Error do
    throw Error { 1, "closed" } if f.pos < 0
    ret f.pos

fn write(f: @F, s: String) throws Error do
    f.pos += s.len as Long

fn flush(f: @F) throws Error do
    pass

fn drive(t: @Teller): Long throws Error do
    try t.write("abc")
    try t.flush()
    ret try t.tell()

fn main() do
    val f = new F { pos = 1 }
    io.log(drive(f) ?? -1)
