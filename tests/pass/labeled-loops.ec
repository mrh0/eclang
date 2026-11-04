use "core:c/stdio.ec"

fn search(): Int do
    var found = -1
    outer: for i in 0..<3 do
        for j in 0..<3 do
            found = i * 10 + j
            break outer if i == 1 and j == 1
    ret found

fn skip() do
    outer: for i in 0..<3 do
        defer printf("end %d\n"c, i)
        for j in 0..<3 do
            defer printf("inner %d %d\n"c, i, j)
            continue outer if j == 1
            printf("body %d %d\n"c, i, j)

fn withElse() do
    outer: for i in 0..<2 do
        var n = 0
        while n < 2 do
            n += 1
            break outer if i == 1
    else do
        printf("not reached\n"c)
    printf("after\n"c)

fn endless(): Int do
    var n = 0
    outer: while true do
        while true do
            n += 1
            if n > 3 do
                break outer
    ret n

fn pools() do
    outer: for i in 0..<2 do
        pool do
            val s = "${i}"
            for j in 0..<2 do
                printf("%.*s %d\n"c, s.len as Int, s.data, j)
                continue outer

fn three(): Int do
    var hits = 0
    a: for i in 0..<3 do
        b: for j in 0..<3 do
            for k in 0..<3 do
                hits += 1
                continue a if k == 1
                break b if j == 2
    ret hits

fn main(): Int do
    printf("%d\n"c, search())
    skip()
    withElse()
    printf("%d\n"c, endless())
    pools()
    printf("%d\n"c, three())
    ret 0
