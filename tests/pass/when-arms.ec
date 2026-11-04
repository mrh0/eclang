use "core:c/stdio.ec"

fn size(n: Int): CString do
    when n do
        ..<0 do ret "negative"c
        0 do ret "zero"c
        1..<10 do ret "small"c
        10..=99 do ret "medium"c
        100.. do ret "large"c
    ret "?"c

fn kind(c: Char): CString = when c do
    'a'..='z' do "lower"c
    'A'..='Z' do "upper"c
    else do "other"c

fn guarded(n: Int): Int = when n do
    x if x > 100 do x - 100
    1, 2 if n > 1 do 20
    y if y < 0 do -y
    else do n

fn unions(v: Int | CString, flag: Bool): CString = when v do
    is Int if flag do "flagged"c
    is Int do "int"c
    is CString do "text"c

fn calls(): Int do
    printf("called\n"c)
    ret 7

fn main(): Int do
    printf("%s %s %s %s %s\n"c, size(-1), size(0), size(5), size(50), size(500))
    printf("%s %s %s\n"c, kind('q'), kind('Q'), kind('1'))
    printf("%d %d %d %d %d\n"c, guarded(150), guarded(2), guarded(1), guarded(-3), guarded(5))
    printf("%s %s %s\n"c, unions(1, true), unions(1, false), unions("s"c, true))
    when calls() do
        n if n > 5 do printf("big %d\n"c, n)
        else do printf("small\n"c)
    ret 0
