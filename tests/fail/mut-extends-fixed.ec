// error: Dog is a `mut rec`, but it extends Animal, whose fields are fixed: Dog can be used where Animal is expected, and there its fields must stay as they are. Remove `mut` from Dog, or declare `mut rec Animal` too.
rec Animal as
    legs: Int

mut rec Dog extends Animal as
    name: String

fn main() do
    val d = Dog { 4, "rex" }
