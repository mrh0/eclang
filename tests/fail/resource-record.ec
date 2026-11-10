// error: The Transaction begin(...) returns is dropped, but Transaction is a `resource`
resource rec Transaction as
    id: Int

fn begin(): Transaction = Transaction { 1 }

fn main() do
    begin()
