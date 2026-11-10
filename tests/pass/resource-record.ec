// A `resource` record is passed on: here to the function that ends it.
use "core:io.ec"

resource rec Transaction as
    id: Int

fn begin(id: Int): Transaction = Transaction { id }

fn commit(t: Transaction) do
    io.log("committed ${t.id}")

fn main() do
    val t = begin(1)
    commit(t)
    commit(begin(2))
