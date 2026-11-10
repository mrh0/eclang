use "core:io.ec"
use "core:path.ec"
use "core:string.ec"
use "core:csv.ec"

fn show(rows: String[][]) do
    io.log("${rows.len} row(s)")
    for row in rows do
        val parts = new String[row.len]
        for i, field in row do
            parts[i] = quote(field)
        io.log(join(parts, " | "))

fn bad(text: String) do
    try csv.parse(text) catch e do
        io.log("${e.message} (${e.line}:${e.column} at ${e.position})")

fn main(): Int throws Error do
    // The examples of RFC 4180.
    show(try csv.parse("aaa,bbb,ccc\r\nzzz,yyy,xxx\r\n"))
    show(try csv.parse("aaa,bbb,ccc\r\nzzz,yyy,xxx"))
    show(try csv.parse("\"aaa\",\"bbb\",\"ccc\"\r\nzzz,yyy,xxx"))
    show(try csv.parse("\"aaa\",\"b\r\nbb\",\"ccc\"\r\nzzz,yyy,xxx"))
    show(try csv.parse("\"aaa\",\"b\"\"bb\",\"ccc\""))

    // Line breaks, empty fields, and what is at the end.
    show(try csv.parse("a\nb\rc\r\nd"))
    show(try csv.parse(""))
    show(try csv.parse("\n"))
    show(try csv.parse("a,b,\n"))
    show(try csv.parse("a,b,"))
    show(try csv.parse(",,"))
    show(try csv.parse("\"\",\"\""))
    show(try csv.parse("a\n\nb\n"))
    show(try csv.parse("a\n\n\r\nb\n\n", csv.Options { skipBlankLines = true }))
    show(try csv.parse(" a , b "))
    show(try csv.parse("a,b\nc\nd,e,f\n"))
    show(try csv.parse("\xEF\xBB\xBFa,b\n"))
    show(try csv.parse("\"\"\"\""))
    show(try csv.parse("\"a\"\"\"\"b\",\"\"\"x\",\"y\"\"\""))

    // Mistakes say where they are, past line breaks in fields too.
    bad("a,b\nc,\"d")
    bad("a,\"b\"c\n")
    bad("a,b\"c\n")
    bad("\"a\nb\nc\" x")
    bad("a\r\nb\r\n\"c\"\"")
    try csv.table("") catch e1 do
        io.log(e1.message)
    try csv.table("a,b\n1,2\n3\n") catch e2 do
        io.log(e2.message)
    try csv.table("a,b\n\n\n1,2,3\n") catch e3 do
        io.log(e3.message)

    // Lenient reading keeps what strict reading rejects.
    val loose = csv.Options { strict = false }
    show(try csv.parse("a,b\"c,\"d\"e,\"f\"\"g\"h\n", loose))
    show(try csv.parse("\"a\"b", loose))

    // Other separators and quotes.
    show(try csv.parse("a\tb,c\t\"d\te\"", csv.Options { separator = '\t' }))
    show(try csv.parse("a;'b;c';'d''e'", csv.Options { separator = ';', quote = '\'' }))

    // One row at a time.
    val r = csv.reader("a,b\nc\n")
    while true do
        val row = try csv.next(r) ?? break
        io.log(row.len)
    io.log((try csv.next(r)) == null)

    // A table.
    val t = try csv.table("name,age\n\nAda,36\n\"Hopper, Grace\",85\n")
    io.log(t.rows.len)
    io.log(csv.value(t, 1, "name") ?? "?")
    io.log(csv.value(t, 0, "age") ?? "?")
    io.log(csv.value(t, 2, "age") ?? "no row")
    io.log(csv.value(t, 0, "city") ?? "no column")
    io.log(join(csv.column(t, "age") ?? new String[0], ";"))
    io.log(csv.column(t, "city") == null)
    io.log(csv.columnIndex(t, "age") ?? 99)
    io.log(quote(csv.stringify(t)))
    val twice = try csv.table("a,a\n1,2\n")
    io.log(csv.value(twice, 0, "a") ?? "?")

    // Writing quotes only what needs it, and all of it comes back.
    io.log(quote(csv.line(["plain", "a,b", "c\"d", "e\nf", "g\rh", " i "])))
    io.log(quote(csv.line([""])))
    io.log(quote(csv.line(["", ""])))
    io.log(quote(csv.line(["a", "b"], csv.Options { separator = ';', lineBreak = "\n" })))
    val odd: String[][] = [["", "a,b"], [""], ["\"", "\"\""], ["x\r\ny", "\n"], ["é", " sp "]]
    val text = csv.stringify(odd)
    io.log(quote(text))
    show(try csv.parse(text))
    val tabbed = csv.Options { separator = '\t', quote = '\'' }
    show(try csv.parse(csv.stringify(odd, tabbed), tabbed))

    // Files.
    val file = path.join(try io.tempDir(), "eclang-std-csv.csv")
    try csv.writeFile(file, [["a,b", "c"], ["1", "2"]])
    show(try csv.readFile(file))
    try io.remove(file)
    try csv.readFile("/nonexistent/x.csv") catch e4 do
        io.log(e4.code == ERR_NOT_FOUND)
    ret 0
