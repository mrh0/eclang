// error: UserId is a `distinct` type, so nothing converts to or from it by itself; convert it explicitly with `as UserId`
type UserId = distinct Int

fn main(): Int do
    val n = 5
    val id: UserId = n
    ret 0
