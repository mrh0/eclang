// Runs SQL against a database file, as the sqlite3 shell does: each statement in turn, printing the rows of those that
// return some, and how many rows the others changed.
//   java -jar target/eclang-0.1.0.jar run bindings/sqlite/examples/sql.ec -- shop.db "CREATE TABLE t (x); INSERT INTO t VALUES (1), (2); SELECT * FROM t"
use "../sqlite.ec"
use "core:io.ec"

/** Runs one statement: its rows, with a header of the column names, or what it changed. */
fn run(db: @sqlite3, stmt: @sqlite3_stmt) throws SqliteError do
    val columns = sqlite3_column_count(stmt)
    if columns == 0 do
        try sqliteStep(stmt)
        io.println("(${sqlite3_changes64(db)} row(s) changed)")
        ret
    var header = ""
    for i in 0..<columns do
        header = if i == 0 do sqliteColumnName(stmt, i) else "${header} | ${sqliteColumnName(stmt, i)}"
    io.println(header)
    var rows = 0
    while true do
        break if not try sqliteStep(stmt)
        var line = ""
        for i in 0..<columns do
            val value = sqliteText(stmt, i) ?? "NULL"
            line = if i == 0 do value else "${line} | ${value}"
        io.println(line)
        rows += 1
    io.println("(${rows} row(s))")

/**
 * Runs every statement of sql. sqlite3_prepare_v2 compiles the first one and sets `tail` to where the next one
 * starts, in the same text, so the text is made a CString (which ends with a 0) and `tail` is read as one.
 */
fn runAll(db: @sqlite3, sql: String) throws SqliteError do
    var rest = cstr(sql)
    while true do
        var stmt: @sqlite3_stmt? = null
        var tail: Char[]c? = null
        try sqliteCheck(db, sqlite3_prepare_v2(db, rest !as Char[]c, -1, mut stmt, mut tail), "can't prepare the SQL")
        // Only spaces or comments were left: no statement.
        val s = stmt ?? break
        defer sqlite3_finalize(s)
        try run(db, s)
        rest = (tail ?? break) !as CString

fn main() throws SqliteError do
    val words = io.args()
    if words.len != 2 do
        io.eprintln("usage: sql DATABASE SQL")
        ret
    val db = try sqliteOpen(words[0])
    defer sqlite3_close(db)
    try runAll(db, words[1])
