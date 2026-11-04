use "core:lib.ec"

fn same(values: Int[]): Int[] = values

fn main(): Int do
    val numbers = [7, 8, 9]
    // Values, then index and value.
    val names = ["ada"c, "bob"c]
    for name in names do
        log(name)
    for i, name in names do
        log(i)
        log(name)
    // A String gives its Chars.
    for ch in "hi" do
        log(ch)
    // Anything but a variable is evaluated once.
    for n in same(numbers) do
        log(n)
    // Ranges: `..<` stops before the end, `..=` includes it.
    for i in 0..<3 do
        log(i)
    for i in 1..=3 do
        log(i)
    for i in 5..<5 do
        log(i)
    // `where` skips rounds; the body sees what the condition proves.
    val maybe: Int?[] = [1, null, 3]
    for m in maybe where m != null do
        log(m + 100)
    for i in 0..<10 where i % 3 == 0 do
        log(i)
    // `else` runs when the loop ends without `break`.
    for n in same(numbers) do
        break if n == 8
    else do
        log("no break"c)
    for n in same(numbers) where n > 100 do
        log(n)
    else do
        log("none over 100"c)
    ret 0
