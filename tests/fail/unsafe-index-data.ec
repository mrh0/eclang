// error: Index the array itself (`a[i]`, which is checked), or write `unsafe a.data[i]`
fn main() do
    val a = [1, 2, 3]
    a.data[1] = 5
