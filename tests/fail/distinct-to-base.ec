// error: UserId is a `distinct` type
type UserId = distinct Int

fn f(n: Int): Int = n

fn main(): Int do
    val id: UserId = 5
    ret f(id)
