// error: never null, so `??` is not needed
fn id(n: Int): Int = n
fn stop() do
    pass
fn main() do
    id(1) ?? stop()
