use "core:io.ec"

fn same(values: Int[]): Int[] = values

fn main(): Int do
    val numbers = [7, 8, 9]
    // Values, then index and value.
    val names = ["ada"c, "bob"c]
    for name in names do
        io.log(name)
    for i, name in names do
        io.log(i)
        io.log(name)
    // A String gives its Chars.
    for ch in "hi" do
        io.log(ch)
    // Anything but a variable is evaluated once.
    for n in same(numbers) do
        io.log(n)
    // Ranges: `..<` stops before the end, `..=` includes it.
    for i in 0..<3 do
        io.log(i)
    for i in 1..=3 do
        io.log(i)
    for i in 5..<5 do
        io.log(i)
    // `where` skips rounds; the body sees what the condition proves.
    val maybe: Int?[] = [1, null, 3]
    for m in maybe where m != null do
        io.log(m + 100)
    for i in 0..<10 where i % 3 == 0 do
        io.log(i)
    // `else` runs when the loop ends without `break`.
    for n in same(numbers) do
        break if n == 8
    else do
        io.log("no break"c)
    for n in same(numbers) where n > 100 do
        io.log(n)
    else do
        io.log("none over 100"c)
    ret 0
