// A small catalog of books in an SQLite database in memory: tables, a transaction of inserts through one prepared
// statement, a query with a join, an SQL function written in eclang, NULLs, BLOBs and a constraint error.
//   java -jar target/eclang-0.1.0.jar run bindings/sqlite/examples/books.ec
use "../sqlite.ec"
use "core:io.ec"

rec Book as
    title: String
    author: String
    year: Int
    rating: Double?

val BOOKS = [
    Book { "The Left Hand of Darkness", "Ursula K. Le Guin", 1969, 4.5 },
    Book { "The Dispossessed", "Ursula K. Le Guin", 1974, 4.25 },
    Book { "A Wizard of Earthsea", "Ursula K. Le Guin", 1968, null },
    Book { "Kindred", "Octavia E. Butler", 1979, 4.75 },
    Book { "Parable of the Sower", "Octavia E. Butler", 1993, 4.0 },
    Book { "Dune", "Frank Herbert", 1965, 3.5 }
]

/** Adds an author once, and returns their rowid either way. */
fn authorId(db: @sqlite3, find: @sqlite3_stmt, add: @sqlite3_stmt, name: String): Long throws SqliteError do
    try sqliteBind(find, 1, name)
    defer sqlite3_reset(find)
    if try sqliteStep(find) do
        ret sqlite3_column_int64(find, 0)
    try sqliteBind(add, 1, name)
    defer sqlite3_reset(add)
    try sqliteStep(add)
    ret sqlite3_last_insert_rowid(db)

/** Adds every book, in one transaction: all of them or, if one fails, none. */
fn addBooks(db: @sqlite3) throws SqliteError do
    val find = try sqlitePrepare(db, "SELECT id FROM authors WHERE name = ?")
    defer sqlite3_finalize(find)
    val add = try sqlitePrepare(db, "INSERT INTO authors (name) VALUES (?)")
    defer sqlite3_finalize(add)
    val insert = try sqlitePrepare(db, "INSERT INTO books (title, author, year, rating) VALUES (:title, :author, :year, :rating)")
    defer sqlite3_finalize(insert)

    try sqliteExec(db, "BEGIN")
    for b in BOOKS do
        val author = try authorId(db, find, add, b.author)
        try sqliteBind(insert, try sqliteParameter(insert, ":title"), b.title)
        try sqliteBind(insert, try sqliteParameter(insert, ":author"), author)
        try sqliteBind(insert, try sqliteParameter(insert, ":year"), b.year)
        if val r = b.rating do
            try sqliteBind(insert, try sqliteParameter(insert, ":rating"), r)
        else
            sqlite3_bind_null(insert, try sqliteParameter(insert, ":rating"))
        try sqliteStep(insert)
        sqlite3_reset(insert)
    try sqliteExec(db, "COMMIT")

/** initials('Ursula K. Le Guin') is 'U.K.L.G.': an SQL function, called by SQLite with its arguments as values. */
fn initials(ctx: @sqlite3_context, argc: Int, argv: @sqlite3_value[]c) do
    val name = sqliteValueText(sqliteArg(argv, argc, 0)) ?? ret sqliteResult(ctx, null)
    var out = ""
    var start = true
    for c in name do
        if start and c != ' ' do
            out = "${out}${c}."
        start = c == ' '
    sqliteResult(ctx, out)

fn report(db: @sqlite3) throws SqliteError do
    io.println("Books by author, with their average rating:")
    val q = try sqlitePrepare(db, "SELECT initials(a.name), a.name, count(*), avg(b.rating) FROM books b JOIN authors a ON a.id = b.author GROUP BY a.id ORDER BY a.name")
    defer sqlite3_finalize(q)
    while true do
        break if not try sqliteStep(q)
        val short = sqliteText(q, 0) ?? "?"
        val name = sqliteText(q, 1) ?? "?"
        val books = sqlite3_column_int(q, 2)
        // avg of only NULLs is NULL: sqlite3_column_type says which it is.
        val rating = if sqlite3_column_type(q, 3) == SQLITE_NULL do "unrated" else "${sqlite3_column_double(q, 3):.2f}"
        io.println("  ${short:<8} ${name:<20} ${books} book(s), ${rating}")

    io.println("Unrated:")
    val unrated = try sqlitePrepare(db, "SELECT title, year FROM books WHERE rating IS NULL")
    defer sqlite3_finalize(unrated)
    while true do
        break if not try sqliteStep(unrated)
        val title = sqliteText(unrated, 0) ?? "?"
        io.println("  ${title} (${sqlite3_column_int(unrated, 1)})")

/** A BLOB goes in and comes out as bytes. */
fn covers(db: @sqlite3) throws SqliteError do
    try sqliteExec(db, "CREATE TABLE covers (book INTEGER PRIMARY KEY, png BLOB)")
    val add = try sqlitePrepare(db, "INSERT INTO covers VALUES (1, ?)")
    defer sqlite3_finalize(add)
    val bytes: UChar[] = [0x89, 0x50, 0x4e, 0x47]
    try sqliteBindBlob(add, 1, bytes)
    try sqliteStep(add)
    val read = try sqlitePrepare(db, "SELECT png, length(png) FROM covers WHERE book = 1")
    defer sqlite3_finalize(read)
    if try sqliteStep(read) do
        val png = sqliteBlob(read, 0)
        io.println("Cover: ${sqlite3_column_int(read, 1)} bytes, starting with ${png[0]:x} ${png[1]:x}")

fn main() throws SqliteError do
    io.println("SQLite ${sqlite3_libversion()}")
    val db = try sqliteOpen(":memory:")
    defer sqlite3_close(db)
    try sqliteExec(db, "CREATE TABLE authors (id INTEGER PRIMARY KEY, name TEXT NOT NULL UNIQUE)")
    try sqliteExec(db, "CREATE TABLE books (id INTEGER PRIMARY KEY, title TEXT NOT NULL UNIQUE, author INTEGER NOT NULL REFERENCES authors, year INTEGER, rating REAL)")
    try sqliteCheck(db, sqlite3_create_function_v2(db, "initials"c, 1, SQLITE_UTF8 | SQLITE_DETERMINISTIC, null, fn initials, null, null), "can't add initials()")

    try addBooks(db)
    io.println("Added ${BOOKS.len} books")
    try report(db)
    try covers(db)

    // A book that is already there: the UNIQUE constraint fails, as an error value with SQLite's extended code.
    val again = try sqlitePrepare(db, "INSERT INTO books (title, author) VALUES ('Dune', 1)")
    defer sqlite3_finalize(again)
    try sqliteStep(again) catch e do
        io.println("Adding Dune again: ${e.message}")
        io.println("  rc ${e.rc} is SQLITE_CONSTRAINT_UNIQUE: ${e.rc == SQLITE_CONSTRAINT_UNIQUE}, kind ERR_ARGUMENT: ${e.code == ERR_ARGUMENT}")
