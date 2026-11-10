/**
 * Environment variables from a .env file.
 *
 *     use "core:env.ec"
 *     try env.load()                                      // sets what .env holds, except what is already set
 *     val url = try env.require("DATABASE_URL")           // an error that names the variable when it isn't set
 *     val port = env.get("PORT") ?? "8080"
 *
 *     val values = try env.parse("A=1\nB=\"two\\nlines\"\n") // only reads: Map<String, String>
 *     io.log(values.get("B") ?? "")
 *
 * The format, one variable a line:
 *
 *     # a comment
 *     NAME=value                  # spaces around the value go, and so does this comment
 *     export OTHER = value        # "export " in front is allowed, as in a shell script
 *     EMPTY=
 *     SINGLE='as it is: no ${EXPANSION}, no \n'
 *     DOUBLE="escapes \n \t \r \" \\ \$, and ${NAME}"
 *     LINES="a double or single quoted value
 *     may go over several lines"
 *     PATHS=${HOME}/bin:${PATH}   # ${NAME}: a variable set above in the file, or else in the environment
 *
 * A name is letters, digits, `_` and `.`, not starting with a digit. A `#` starts a comment only after a space or at the
 * start of a line, so `COLOR=#fff` keeps its `#`. A name given twice takes its last value. `${NAME}` of something not
 * set is empty; `$NAME` without braces is kept as it is.
 *
 * What is returned is allocated in the current pool. The environment belongs to the whole program: load it before
 * starting threads.
 */
module "env"
use "core:errors.ec"
use "core:string.ec"
use "core:structures.ec"
use "core:io.ec"
use "core:os.ec"

/** Text isn't a valid .env file. */
rec EnvError extends ParseError as
    /** The line of the problem, from 1. */
    line: Size
    /** The column of the problem, in bytes, from 1. */
    column: Size

/** Where {@link parse} is in the text. */
priv mut ref rec Parser as
    /** What is read. */
    text: String
    /** The next byte to read. */
    at: Size
    /** The line of that byte, from 1. */
    line: Size
    /** The byte where that line starts. */
    lineStart: Size
    /** What has been read so far, for `${NAME}`. */
    values: Map<String, String>

/** An {@link EnvError} at byte `at`, which is on the current line. */
priv fn problem(p: Parser, message: String, at: Size): EnvError do
    val column = at - p.lineStart + 1
    ret EnvError { ERR_PARSE, "line ${p.line}, column ${column}: ${message}", p.text, at, p.line, column }

/** Whether the text has ended. */
priv fn ended(p: Parser): Bool = p.at >= p.text.len

/** The current byte, which must exist. */
priv fn peek(p: Parser): Char = p.text[p.at]

/** Goes past spaces and tabs. */
priv fn skipBlanks(p: Parser) do
    while not ended(p) and (peek(p) == ' ' or peek(p) == '\t') do
        p.at += 1

/** Goes past the byte at `p.at`, counting the line when it ends one. */
priv fn advance(p: Parser) do
    val c = peek(p)
    p.at += 1
    if c == '\r' and not ended(p) and peek(p) == '\n' do
        p.at += 1
    if c == '\n' or c == '\r' do
        p.line += 1
        p.lineStart = p.at

/** Goes to the start of the next line. */
priv fn skipLine(p: Parser) do
    while not ended(p) and peek(p) != '\n' and peek(p) != '\r' do
        p.at += 1
    advance(p) if not ended(p)

/** Whether `c` may be in a name; `first` for its first byte. */
priv fn nameChar(c: Char, first: Bool): Bool = isAlpha(c) or c == '_' or (not first and (isDigit(c) or c == '.'))

/** Reads a name at the current byte, or gives "" when there is none. */
priv fn name(p: Parser): String do
    val start = p.at
    while not ended(p) and nameChar(peek(p), p.at == start) do
        p.at += 1
    ret p.text[start..<p.at]

/** The value of the variable `name`: from the file so far, or else the environment, or else "". */
priv fn lookup(p: Parser, name: String): String do
    if val value = p.values.get(name) do
        ret value
    ret os.env(name) ?? ""

/**
 * Adds the value of `${NAME}` to `b`; the current byte is the `$`.
 * @throws when the braces don't hold a name and a `}`
 */
priv fn expand(p: Parser, b: StringBuilder) throws EnvError do
    val start = p.at
    p.at += 2
    val n = name(p)
    throw problem(p, "'\${' needs a variable name and a '}' after it. Write \\$ in double quotes, or use single quotes, for a plain $", start) if n.len == 0 or ended(p) or peek(p) != '}'
    p.at += 1
    b.append(lookup(p, n))

/** Whether the current byte starts `${`. */
priv fn atExpansion(p: Parser): Bool = peek(p) == '$' and p.at + 1 < p.text.len and p.text[p.at + 1] == '{'

/**
 * Reads a quoted value; the current byte is its opening quote.
 * @throws when it doesn't end, or a `${` in double quotes isn't followed by a name
 */
priv fn quoted(p: Parser): String throws EnvError do
    val q = peek(p)
    val start = p.at
    val startLine = p.line
    val startOfLine = p.lineStart
    p.at += 1
    val b = stringBuilder(64)
    while true do
        if ended(p) do
            p.line = startLine
            p.lineStart = startOfLine
            throw problem(p, "this value has no closing ${q}", start)
        val c = peek(p)
        if c == q do
            p.at += 1
            ret b.build()
        if q == '"' and c == '\\' and p.at + 1 < p.text.len do
            val e = p.text[p.at + 1]
            when e do
                'n' do b.append('\n')
                't' do b.append('\t')
                'r' do b.append('\r')
                '"' do b.append('"')
                '\\' do b.append('\\')
                '$' do b.append('$')
                else do
                    // Not an escape: kept as written, as shells and other .env readers do.
                    b.append('\\')
                    b.append(e)
            p.at += 2
        else if q == '"' and atExpansion(p) do
            try expand(p, b)
        else if c == '\n' or c == '\r' do
            b.append('\n')
            advance(p)
        else do
            b.append(c)
            p.at += 1

/**
 * Reads an unquoted value, up to the end of the line or a comment, without the spaces at its end.
 * @throws when a `${` isn't followed by a name
 */
priv fn plain(p: Parser): String throws EnvError do
    val b = stringBuilder(64)
    var kept: Size = 0
    while not ended(p) and peek(p) != '\n' and peek(p) != '\r' do
        val c = peek(p)
        break if c == '#' and p.at > 0 and (p.text[p.at - 1] == ' ' or p.text[p.at - 1] == '\t')
        if atExpansion(p) do
            try expand(p, b)
            kept = b.len
        else do
            b.append(c)
            p.at += 1
            if c != ' ' and c != '\t' do
                kept = b.len
    ret b.build()[0..<kept]

/**
 * Reads one line that isn't empty or a comment, and puts its variable in `p.values`.
 * @throws when it isn't `NAME=value`
 */
priv fn assignment(p: Parser) throws EnvError do
    var key = name(p)
    if key == "export" and not ended(p) and (peek(p) == ' ' or peek(p) == '\t') do
        skipBlanks(p)
        key = name(p)
    if key.len == 0 do
        val what = if ended(p) do "the end" else "'${charString(peek(p))}'"
        throw problem(p, "expected a variable name (letters, digits, _ and ., not starting with a digit), and found ${what}", p.at)
    skipBlanks(p)
    throw problem(p, "'${key}' needs '=' and a value after it: ${key}=value", p.at) if ended(p) or peek(p) != '='
    p.at += 1
    skipBlanks(p)
    var value = ""
    if not ended(p) and (peek(p) == '"' or peek(p) == '\'') do
        value = try quoted(p)
        skipBlanks(p)
        if not ended(p) and peek(p) != '\n' and peek(p) != '\r' and peek(p) != '#' do
            throw problem(p, "only a comment can follow a quoted value. Put the whole value inside the quotes", p.at)
    else do
        value = try plain(p)
    p.values.put(key, value)
    skipLine(p)

/**
 * Reads the text of a .env file, without changing the environment.
 * @param text what the file holds
 * @return each variable and its value, in the order of the file
 * @throws when a line isn't valid
 */
fn parse(text: String): Map<String, String> throws EnvError do
    val start: Size = if text.startsWith("\xEF\xBB\xBF") do 3 else 0
    val p = new Parser { text, start, 1, start, map() }
    while not ended(p) do
        skipBlanks(p)
        if ended(p) do
            break
        val c = peek(p)
        if c == '#' or c == '\n' or c == '\r' do
            skipLine(p)
        else do
            try assignment(p)
    ret p.values

/**
 * Reads a .env file, without changing the environment.
 * @param path where it is
 * @return each variable and its value, in the order of the file
 * @throws when the file can't be read, or isn't valid
 */
fn read(path: String = ".env"): Map<String, String> throws Error do
    val text = try io.readFile(path)
    ret try parse(text)

/**
 * Sets each variable of `values` in the environment, for this program and the programs it starts.
 * @param values the variables, as {@link parse} gives them
 * @param override whether to replace variables that are already set; by default the environment wins
 * @return how many were set
 * @throws when a name can't be set
 */
fn apply(values: Map<String, String>, override: Bool = false): Size throws OsError do
    var count: Size = 0
    for key, value in values do
        if override or os.env(key) == null do
            try os.setEnv(key, value)
            count += 1
    ret count

/**
 * Reads a .env file and sets its variables in the environment.
 * @param path where it is
 * @param override whether to replace variables that are already set; by default the environment wins, so that a
 *     variable given on the command line beats the file
 * @return how many were set
 * @throws when the file can't be read, isn't valid, or a name can't be set
 */
fn load(path: String = ".env", override: Bool = false): Size throws Error do
    val values = try read(path)
    ret try apply(values, override)

/**
 * Like {@link load}, but a file that doesn't exist is no error: the program then runs with the environment it has.
 * @return how many were set, 0 when there is no file
 */
fn loadIfExists(path: String = ".env", override: Bool = false): Size throws Error do
    ret 0 if not io.exists(path)
    ret try load(path, override)

/** The value of the environment variable `name`, or null when it isn't set: the same as `os.env`. */
fn get(name: String): String? = os.env(name)

/**
 * The value of the environment variable `name`.
 * @throws a NotFoundError, which names it, when it isn't set
 */
fn require(name: String): String throws NotFoundError do
    if val value = os.env(name) do
        ret value
    throw NotFoundError { ERR_NOT_FOUND, "the environment variable '${name}' isn't set: set it, or add ${name}=... to the .env file", name }
