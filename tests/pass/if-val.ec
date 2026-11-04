use "core:lib.ec"

fn find(names: CString[], wanted: CString): Size? do
    for i, name in names do
        ret i if name == wanted
    ret null

fn digit(c: Char): Int throws Error do
    throw Error { 1, "not a digit" } if c < '0' or c > '9'
    ret (c - '0') as Int

fn describe(c: Char): Int do
    if val n = digit(c) do
        ret n
    else do
        ret -1

fn main(): Int do
    val names = ["ada"c, "bob"c]
    // A nullable value: the body runs when it isn't null, with the value.
    if val i = find(names, "bob"c) do
        log(i)
    if val i = find(names, "eve"c) do
        log(i)
    else do
        log("no eve"c)
    val maybe: CString? = "here"c
    if val text = maybe do
        log(text)
    // A call that can fail: the body runs when it succeeded.
    if val n = digit('7') do
        log(n)
    log(describe('x'))
    // `if var` gives a variable.
    val count: Int? = 4
    if var c = count do
        c = c * 10
        log(c)
    ret 0
