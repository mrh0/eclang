use "core:c/stdio.ec"
use "lib/ids.ec"

fn main(): Int do
    val a: ids.Id = 1
    val b: ids.Pair = ids.next(a)
    when b do
        is ids.Id do printf("%d\n"c, b as Int)
        is CString do printf("text\n"c)
    ret 0
