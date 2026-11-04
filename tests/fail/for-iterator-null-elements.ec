// error: would take the first null for the end
use "core:structures.ec"
fn main() do
    val q: @Deque<Int?> = deque()
    for x in q do
        pass
