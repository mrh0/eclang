// error: 'pick' changes 'p' (`mut p`), and may store in it what its other arguments point to. Can't store this in 'p': it points to 'b'

fn pick(mut p: @Int, q: @Int) do
    p = q

fn main() do
    var a = 1
    var p = addrof a
    if a > 0 do
        var b = 2
        pick(mut p, addrof b)
    @p = 3
