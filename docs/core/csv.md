# Module `csv`

CSV (RFC 4180): reading and writing tables of text, with error positions.

    use "core:csv.ec"
    val rows = try csv.parse("name,age\nAda,36\n\"Hopper, Grace\",85\n")
    io.log(rows[2][0])                                  // Hopper, Grace

    val t = try csv.table("name,age\nAda,36\n")         // the first row names the columns
    io.log(csv.value(t, 0, "age") ?? "?")               // 36
    for name in csv.column(t, "name") ?? new String[0] do
        io.log(name)

    val r = csv.reader(try io.readFile("big.csv"))      // a row at a time
    while true do
        val row = try csv.next(r) ?? break
        io.log(row.len)

    io.log(csv.stringify([["a,b", "c"], ["1", "2"]]))   // "a,b",c   and   1,2
    try csv.writeFile("out.csv", rows)

    val tabs = csv.Options { separator = '\t' }         // the same, with tabs
    val cells = try csv.parse(text, tabs)

Reading follows RFC 4180, with these additions, which are common in real files: a line may end with "\n" or "\r" as
well as "\r\n", the last line needn't end with a line break, and a UTF-8 byte order mark at the start is skipped. Fields are
text: nothing is converted to numbers, and spaces belong to the field. Rows may have different lengths, except in a
[`Table`](#csv.Table). An empty line is a row with one empty field, as the RFC's grammar says; [`Options`](#csv.Options) can skip them.

Writing uses "\r\n", as the RFC says, and quotes only the fields that need it. A field without a doubled quote is a
slice of the text, sharing its memory; everything else is allocated in the current pool.

## Types

### <a id="csv.Options"></a>`Options`

```ec
rec Options
```

How a file is written. The defaults are those of RFC 4180.

**Fields**

- <a id="csv.Options.separator"></a>`separator: Char = ','` — What is between the fields of a row: `'\t'` and `';'` are common too.
- <a id="csv.Options.quote"></a>`quote: Char = '"'` — What is around a field that needs it, doubled inside it.
- <a id="csv.Options.lineBreak"></a>`lineBreak: String = "\r\n"` — What ends a row when writing; reading takes all of them.
- <a id="csv.Options.skipBlankLines"></a>`skipBlankLines: Bool = false` — Whether reading skips empty lines, instead of making a row with one empty field of each.
- <a id="csv.Options.strict"></a>`strict: Bool = true` — Whether reading gives an error for a quote where RFC 4180 doesn't allow one. When false, a quote inside an unquoted
  field is an ordinary character, and what follows a closing quote joins the field: `a"b` and `"a"b`.

### <a id="csv.CsvError"></a>`CsvError`

```ec
rec CsvError extends ParseError
```

Text isn't valid CSV.

**Fields**

- <a id="csv.CsvError.line"></a>`line: Size` — The line of the problem, from 1.
- <a id="csv.CsvError.column"></a>`column: Size` — The column of the problem, in bytes, from 1.

### <a id="csv.Reader"></a>`Reader`

```ec
rec Reader
```

Reads CSV a row at a time: [`reader`](#csv.reader).

**Fields**

- <a id="csv.Reader.text"></a>`text: String` — What is read.
- <a id="csv.Reader.options"></a>`options: Options` — How it is read.
- <a id="csv.Reader.at"></a>`at: Size` — The next byte to read.
- <a id="csv.Reader.line"></a>`line: Size` — The line of that byte, from 1.
- <a id="csv.Reader.lineStart"></a>`lineStart: Size` — The byte where that line starts.
- <a id="csv.Reader.rowLine"></a>`rowLine: Size = 1` — The line where the last row started, from 1.
- <a id="csv.Reader.rowStart"></a>`rowStart: Size = 0` — The byte where the last row started.

### <a id="csv.Table"></a>`Table`

```ec
rec Table
```

CSV whose first row names the columns: [`table`](#csv.table).

**Fields**

- <a id="csv.Table.header"></a>`header: String[]` — The names of the columns.
- <a id="csv.Table.rows"></a>`rows: String[][]` — The rows after the first, each with a field for every column.
- <a id="csv.Table.columns"></a>`columns: @Map<String, Size>` — Where each name is in the header.

## Functions

### <a id="csv.rfc4180"></a>`rfc4180`

```ec
fn rfc4180(): Options
```

The [`Options`](#csv.Options) of RFC 4180, which is what is used when no others are given.

### <a id="csv.reader"></a>`reader`

```ec
fn reader(text: String, options: Options = rfc4180()): @Reader
```

A [`Reader`](#csv.Reader) of `text`, in the current pool.

**Parameters**

- `text` — the CSV
- `options` — how to read it

### <a id="csv.next"></a>`next`

```ec
fn next(r: @Reader): String[]? throws CsvError
```

Reads the next row.

**Returns** its fields, at least one; null when the text has ended

**Throws**

- `CsvError` — when the row isn't valid

### <a id="csv.parse"></a>`parse`

```ec
fn parse(text: String, options: Options = rfc4180()): String[][] throws CsvError
```

Reads all of `text`.

**Parameters**

- `text` — the CSV
- `options` — how to read it

**Returns** the rows, each an array of its fields

**Throws**

- `CsvError` — when it isn't valid

### <a id="csv.readFile"></a>`readFile`

```ec
fn readFile(path: String, options: Options = rfc4180()): String[][] throws Error
```

Reads a whole CSV file.

**Parameters**

- `path` — where it is
- `options` — how to read it

**Returns** the rows, each an array of its fields

**Throws**

- `Error` — when the file can't be read, or isn't valid

### <a id="csv.table"></a>`table`

```ec
fn table(text: String, options: Options = rfc4180()): @Table throws CsvError
```

Reads CSV whose first row names the columns, skipping empty lines.

**Parameters**

- `text` — the CSV
- `options` — how to read it

**Returns** the table, which is in the current pool

**Throws**

- `CsvError` — when it isn't valid, has no header, or a row doesn't have as many fields as the header

### <a id="csv.columnIndex"></a>`columnIndex`

```ec
fn columnIndex(t: @Table, name: String): Size?
```

Where the column `name` is, or null when there is none.

### <a id="csv.value"></a>`value`

```ec
fn value(t: @Table, row: Size, name: String): String?
```

The field of a column in a row: `csv.value(t, 0, "age")`.

**Parameters**

- `t` — the table
- `row` — the row, from 0, not counting the header
- `name` — the column

**Returns** the field; null when there is no such row or column

### <a id="csv.column"></a>`column`

```ec
fn column(t: @Table, name: String): String[]?
```

All the fields of the column `name`, or null when there is no such column.

### <a id="csv.line"></a>`line`

```ec
fn line(fields: String[], options: Options = rfc4180()): String
```

One row as CSV, with its line break, for writing a row at a time: `csv.line(["a,b", "c"])` is `"a,b",c` and "\r\n".

**Parameters**

- `fields` — the fields
- `options` — how to write it

### <a id="csv.stringify"></a>`stringify`

```ec
fn stringify(rows: String[][], options: Options = rfc4180()): String
```

The rows as CSV.

**Parameters**

- `rows` — the rows, each an array of its fields
- `options` — how to write them

### `stringify`

```ec
fn stringify(t: @Table, options: Options = rfc4180()): String
```

A table as CSV, header first.

**Parameters**

- `t` — the table
- `options` — how to write it

### <a id="csv.writeFile"></a>`writeFile`

```ec
fn writeFile(path: String, rows: String[][], options: Options = rfc4180()) throws IoError
```

Writes the rows to a file, replacing what it holds.

**Parameters**

- `path` — where it goes
- `rows` — the rows, each an array of its fields
- `options` — how to write them

**Throws**

- `IoError` — when the file can't be written
