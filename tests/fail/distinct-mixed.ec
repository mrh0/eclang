// error: Can't combine UserId and Int
type UserId = distinct Int

fn main(): Int do
    val id: UserId = 5
    val n = 1
    val s = id + n
    ret 0
