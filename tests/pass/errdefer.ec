use "core:c/stdio.ec"
use "core:errors.ec"

fn fail(really: Bool): Int throws Error do
    if really do
        throw Error { 1, "nope" }
    ret 1

fn open(fails: Bool): Int throws Error do
    printf("open\n"c)
    errdefer printf("undo\n"c)
    defer printf("always\n"c)
    val v = try fail(fails)
    ret v + 1

fn thrown(): Int throws Error do
    errdefer printf("undo 1\n"c)
    errdefer do
        printf("undo 2\n"c)
    throw Error { 2, "thrown" }

fn inner(leave: Bool): Int throws Error do
    if leave do
        errdefer printf("not reached\n"c)
    errdefer printf("outer undo\n"c)
    for i in 0..<2 do
        errdefer printf("loop undo\n"c)
        continue if i == 0
        val v = try fail(leave)
    ret 3

fn jump(id: CString?): Int throws Error do
    errdefer printf("jump undo\n"c)
    val v = id ?? throw Error { 3, "none" }
    ret 4

fn main(): Int do
    printf("%d\n"c, open(false) ?? -1)
    printf("%d\n"c, open(true) ?? -1)
    printf("%d\n"c, thrown() ?? -1)
    printf("%d\n"c, inner(true) ?? -1)
    printf("%d\n"c, inner(false) ?? -1)
    printf("%d\n"c, jump(null) ?? -1)
    printf("%d\n"c, jump("x"c) ?? -1)
    ret 0
