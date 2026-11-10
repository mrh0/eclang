/**
 * CSV (RFC 4180): reading and writing tables of text, with error positions.
 *
 *     use "core:csv.ec"
 *     val rows = try csv.parse("name,age\nAda,36\n\"Hopper, Grace\",85\n")
 *     io.log(rows[2][0])                                  // Hopper, Grace
 *
 *     val t = try csv.table("name,age\nAda,36\n")         // the first row names the columns
 *     io.log(csv.value(t, 0, "age") ?? "?")               // 36
 *     for name in csv.column(t, "name") ?? new String[0] do
 *         io.log(name)
 *
 *     val r = csv.reader(try io.readFile("big.csv"))      // a row at a time
 *     while true do
 *         val row = try csv.next(r) ?? break
 *         io.log(row.len)
 *
 *     io.log(csv.stringify([["a,b", "c"], ["1", "2"]]))   // "a,b",c   and   1,2
 *     try csv.writeFile("out.csv", rows)
 *
 *     val tabs = csv.Options { separator = '\t' }         // the same, with tabs
 *     val cells = try csv.parse(text, tabs)
 *
 * Reading follows RFC 4180, with these additions, which are common in real files: a line may end with "\n" or "\r" as
 * well as "\r\n", the last line needn't end with a line break, and a UTF-8 byte order mark at the start is skipped. Fields are
 * text: nothing is converted to numbers, and spaces belong to the field. Rows may have different lengths, except in a
 * {@link Table}. An empty line is a row with one empty field, as the RFC's grammar says; {@link Options} can skip them.
 *
 * Writing uses "\r\n", as the RFC says, and quotes only the fields that need it. A field without a doubled quote is a
 * slice of the text, sharing its memory; everything else is allocated in the current pool.
 */
module "csv"
use "core:errors.ec"
use "core:string.ec"
use "core:structures.ec"
use "core:io.ec"

/** How a file is written. The defaults are those of RFC 4180. */
rec Options as
    /** What is between the fields of a row: `'\t'` and `';'` are common too. */
    separator: Char = ','
    /** What is around a field that needs it, doubled inside it. */
    quote: Char = '"'
    /** What ends a row when writing; reading takes all of them. */
    lineBreak: String = "\r\n"
    /** Whether reading skips empty lines, instead of making a row with one empty field of each. */
    skipBlankLines: Bool = false
    /**
     * Whether reading gives an error for a quote where RFC 4180 doesn't allow one. When false, a quote inside an unquoted
     * field is an ordinary character, and what follows a closing quote joins the field: `a"b` and `"a"b`.
     */
    strict: Bool = true

/** The {@link Options} of RFC 4180, which is what is used when no others are given. */
fn rfc4180(): Options = Options { separator = ',' }

/** Text isn't valid CSV. */
rec CsvError extends ParseError as
    /** The line of the problem, from 1. */
    line: Size
    /** The column of the problem, in bytes, from 1. */
    column: Size

// ---------------------------------------------------------------------------------------------------
// Reading

/** Reads CSV a row at a time: {@link reader}. */
mut ref rec Reader as
    /** What is read. */
    text: String
    /** How it is read. */
    options: Options
    /** The next byte to read. */
    at: Size
    /** The line of that byte, from 1. */
    line: Size
    /** The byte where that line starts. */
    lineStart: Size
    /** The line where the last row started, from 1. */
    rowLine: Size = 1
    /** The byte where the last row started. */
    rowStart: Size = 0

/** A {@link CsvError} at byte `at` of a line that starts at `lineStart`. */
priv fn problem(r: Reader, message: String, at: Size, line: Size, lineStart: Size): CsvError do
    val column = at - lineStart + 1
    ret CsvError { ERR_PARSE, "line ${line}, column ${column}: ${message}", r.text, at, line, column }

/**
 * A {@link Reader} of `text`, in the current pool.
 * @param text the CSV
 * @param options how to read it
 */
fn reader(text: String, options: Options = rfc4180()): Reader do
    val start: Size = if text.startsWith("\xEF\xBB\xBF") do 3 else 0
    ret new Reader { text, options, start, 1, start }

/** Whether the line break at the current byte is there, and goes past it, counting the line. */
priv fn skipLineBreak(r: Reader): Bool do
    ret false if r.at >= r.text.len
    val c = r.text[r.at]
    ret false if c != '\n' and c != '\r'
    r.at += if c == '\r' and r.at + 1 < r.text.len and r.text[r.at + 1] == '\n' do 2 else 1
    r.line += 1
    r.lineStart = r.at
    ret true

/**
 * Reads the field at the current byte, an opening quote.
 * @throws when it doesn't end, or something other than a separator or line break comes after it
 */
priv fn quotedField(r: Reader): String throws CsvError do
    val q = r.options.quote
    val startAt = r.at
    val startLine = r.line
    val startOfLine = r.lineStart
    r.at += 1
    val start = r.at
    // Only a doubled quote or lenient reading needs a copy. Until then this is a slice of the text.
    var copy: StringBuilder? = null
    var copied = start
    while true do
        throw problem(r, "this quoted field has no closing ${q}", startAt, startLine, startOfLine) if r.at >= r.text.len
        val c = r.text[r.at]
        if c == q do
            if r.at + 1 < r.text.len and r.text[r.at + 1] == q do
                val b = copy ?? stringBuilder(64)
                copy = b
                b.append(r.text[copied..<r.at + 1])
                r.at += 2
                copied = r.at
            else do
                break
        else if c == '\n' or c == '\r' do
            skipLineBreak(r)
        else do
            r.at += 1
    val end = r.at
    r.at += 1
    if r.at < r.text.len and not atFieldEnd(r) do
        throw problem(r, "only ${r.options.separator} or a line break can follow a closing ${q}. A ${q} inside a field is written twice", r.at, r.line, r.lineStart) if r.options.strict
        val rest = r.at
        while r.at < r.text.len and not atFieldEnd(r) do
            r.at += 1
        val b2 = copy ?? stringBuilder(64)
        b2.append(r.text[copied..<end])
        b2.append(r.text[rest..<r.at])
        ret b2.build()
    if val b3 = copy do
        b3.append(r.text[copied..<end])
        ret b3.build()
    ret r.text[start..<end]

/** Whether the current byte, which must exist, ends a field. */
priv fn atFieldEnd(r: Reader): Bool do
    val c = r.text[r.at]
    ret c == r.options.separator or c == '\n' or c == '\r'

/**
 * Reads the field at the current byte, which isn't a quote.
 * @throws when it has a quote in it, in strict reading
 */
priv fn plainField(r: Reader): String throws CsvError do
    val start = r.at
    while r.at < r.text.len and not atFieldEnd(r) do
        throw problem(r, "a ${r.options.quote} in a field needs quotes around the whole field, and is written twice", r.at, r.line, r.lineStart) if r.text[r.at] == r.options.quote and r.options.strict
        r.at += 1
    ret r.text[start..<r.at]

/**
 * Reads the next row.
 * @return its fields, at least one; null when the text has ended
 * @throws when the row isn't valid
 */
fn next(r: Reader): String[]? throws CsvError do
    if r.options.skipBlankLines do
        while skipLineBreak(r) do
            continue
    ret null if r.at >= r.text.len
    r.rowLine = r.line
    r.rowStart = r.at
    val fields: List<String> = list()
    while true do
        if r.at < r.text.len and r.text[r.at] == r.options.quote do
            fields.push(try quotedField(r))
        else do
            fields.push(try plainField(r))
        break if r.at >= r.text.len
        if r.text[r.at] == r.options.separator do
            r.at += 1
        else do
            skipLineBreak(r)
            break
    ret fields.items()

/**
 * Reads all of `text`.
 * @param text the CSV
 * @param options how to read it
 * @return the rows, each an array of its fields
 * @throws when it isn't valid
 */
fn parse(text: String, options: Options = rfc4180()): String[][] throws CsvError do
    val r = reader(text, options)
    val rows: List<String[]> = list()
    while true do
        val row = try next(r) ?? break
        rows.push(row)
    ret rows.items()

/**
 * Reads a whole CSV file.
 * @param path where it is
 * @param options how to read it
 * @return the rows, each an array of its fields
 * @throws when the file can't be read, or isn't valid
 */
fn readFile(path: String, options: Options = rfc4180()): String[][] throws Error do
    val text = try io.readFile(path)
    ret try parse(text, options)

// ---------------------------------------------------------------------------------------------------
// Tables

/** CSV whose first row names the columns: {@link table}. */
ref rec Table as
    /** The names of the columns. */
    header: String[]
    /** The rows after the first, each with a field for every column. */
    rows: String[][]
    /** Where each name is in the header. */
    columns: Map<String, Size>

/**
 * Reads CSV whose first row names the columns, skipping empty lines.
 * @param text the CSV
 * @param options how to read it
 * @return the table, which is in the current pool
 * @throws when it isn't valid, has no header, or a row doesn't have as many fields as the header
 */
fn table(text: String, options: Options = rfc4180()): Table throws CsvError do
    val r = reader(text, Options { ...options, skipBlankLines = true })
    val header = try next(r) ?? throw problem(r, "a table needs a header row, and this text has none", r.at, r.line, r.lineStart)
    val columns: Map<String, Size> = map()
    for i, name in header do
        // A name used twice means its first column, which is what a reader would look at first.
        columns.put(name, i) if not columns.has(name)
    val rows: List<String[]> = list()
    while true do
        val row = try next(r) ?? break
        throw problem(r, "this row has ${row.len} field(s), and the header has ${header.len}", r.rowStart, r.rowLine, r.rowStart) if row.len != header.len
        rows.push(row)
    ret new Table { header, rows.items(), columns }

/** Where the column `name` is, or null when there is none. */
fn columnIndex(t: Table, name: String): Size? = t.columns.get(name)

/**
 * The field of a column in a row: `csv.value(t, 0, "age")`.
 * @param t the table
 * @param row the row, from 0, not counting the header
 * @param name the column
 * @return the field; null when there is no such row or column
 */
fn value(t: Table, row: Size, name: String): String? do
    ret null if row >= t.rows.len
    val at = columnIndex(t, name) ?? ret null
    ret t.rows[row][at]

/** All the fields of the column `name`, or null when there is no such column. */
fn column(t: Table, name: String): String[]? do
    val at = columnIndex(t, name) ?? ret null
    val out = new String[t.rows.len]
    for i, row in t.rows do
        out[i] = row[at]
    ret out

// ---------------------------------------------------------------------------------------------------
// Writing

/** Whether `s` needs quotes around it. */
priv fn needsQuotes(s: String, options: Options): Bool do
    for c in s do
        ret true if c == options.separator or c == options.quote or c == '\n' or c == '\r'
    ret false

/** Adds a field to `b`, in quotes when it needs them. */
priv fn addField(b: StringBuilder, s: String, options: Options) do
    if needsQuotes(s, options) do
        b.append(options.quote)
        for c in s do
            b.append(options.quote) if c == options.quote
            b.append(c)
        b.append(options.quote)
    else do
        b.append(s)

/** Adds a row and its line break to `b`. */
priv fn addRow(b: StringBuilder, fields: String[], options: Options) do
    // A row with one empty field would be an empty line, which readers may skip.
    if fields.len == 1 and fields[0].len == 0 do
        b.append(options.quote)
        b.append(options.quote)
    for i, field in fields do
        b.append(options.separator) if i > 0
        addField(b, field, options)
    b.append(options.lineBreak)

/**
 * One row as CSV, with its line break, for writing a row at a time: `csv.line(["a,b", "c"])` is `"a,b",c` and "\r\n".
 * @param fields the fields
 * @param options how to write it
 */
fn line(fields: String[], options: Options = rfc4180()): String do
    val b = stringBuilder(64)
    addRow(b, fields, options)
    ret b.build()

/**
 * The rows as CSV.
 * @param rows the rows, each an array of its fields
 * @param options how to write them
 */
fn stringify(rows: String[][], options: Options = rfc4180()): String do
    val b = stringBuilder(256)
    for row in rows do
        addRow(b, row, options)
    ret b.build()

/**
 * A table as CSV, header first.
 * @param t the table
 * @param options how to write it
 */
fn stringify(t: Table, options: Options = rfc4180()): String do
    val b = stringBuilder(256)
    addRow(b, t.header, options)
    for row in t.rows do
        addRow(b, row, options)
    ret b.build()

/**
 * Writes the rows to a file, replacing what it holds.
 * @param path where it goes
 * @param rows the rows, each an array of its fields
 * @param options how to write them
 * @throws when the file can't be written
 */
fn writeFile(path: String, rows: String[][], options: Options = rfc4180()) throws IoError do
    try io.writeFile(path, stringify(rows, options))
