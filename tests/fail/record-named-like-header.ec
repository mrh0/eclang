// error: is already used by C
use "core:net.ec"

rec sockaddr_in as
    a: Int

fn main() do
    val x = sockaddr_in { 1 }
