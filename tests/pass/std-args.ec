use "core:io.ec"
use "core:args.ec"
use "core:string.ec"

fn makeParser(): args.Parser do
    val p = args.parser("tool", "Copies files around.")
    args.flag(p, "verbose", "Say more", 'v')
    args.flag(p, "dry-run", "Change nothing")
    args.option(p, "output", "Where to write", 'o', "FILE", "out.txt")
    args.option(p, "jobs", "How many at once", 'j', "N")
    val build = args.command(p, "build", "Build a target")
    args.positional(build, "target", "What to build")
    args.positional(build, "mode", "How", false)
    args.rest(build, "more", "Other targets")
    args.flag(build, "release", "Optimise", 'r')
    args.requiredOption(build, "level", "Level", 'l', "N")
    args.command(p, "test", "Run the tests")
    ret p

fn count(r: args.Parsed, name: String): String do
    val n = args.times(r, name)
    ret "${n}"

fn yes(r: args.Parsed, name: String): String = if args.isSet(r, name) do "yes" else "no"

fn field(name: String, value: String?) do
    io.print("${name}=")
    io.print(value ?? "-")
    io.print(" ")

fn show(r: args.Parsed) throws args.ArgsError do
    if val text = r.help do
        io.print(text)
    else do
        field("verbose", count(r, "verbose"))
        field("output", args.value(r, "output"))
        field("dry", yes(r, "dry-run"))
        if val jobs = try args.longValue(r, "jobs") do
            io.print("jobs=${jobs} ")
        if val sub = r.sub do
            field("command", r.command)
            field("target", args.value(sub, "target"))
            field("mode", args.value(sub, "mode"))
            field("more", join(args.valuesOf(sub, "more"), "+"))
            field("release", yes(sub, "release"))
            field("level", args.value(sub, "level"))
            field("sees-verbose", yes(sub, "verbose"))
        io.println("")

fn attempt(line: String) do
    io.log("$ tool ${line}")
    val words = if line.len == 0 do new String[0] else line.split(' ')
    try parseAndShow(words) catch problem do
        io.log("error: ${problem.message} | ${problem.argument} | ${problem.hint}")

fn parseAndShow(words: String[]) throws args.ArgsError do
    try show(try args.parse(makeParser(), words))

fn main(): Int do
    attempt("--help")
    attempt("build --help")
    attempt("-h build")
    attempt("test")
    attempt("-vvv --dry-run -ofile --jobs=4 test")
    attempt("-v -o x --output y -j 2 build a b c d -r -l=3")
    attempt("build a --level -1")
    attempt("build a --level 2 -- -x --y")
    attempt("build -1 - --level 2")
    attempt("build a -l2 -rl3")
    attempt("--verbos test")
    attempt("--zzzzzz test")
    attempt("-x test")
    attempt("--verbose=1 test")
    attempt("--output")
    attempt("-o")
    attempt("bild")
    attempt("zzzzzz")
    attempt("")
    attempt("build")
    attempt("build a")
    attempt("test extra")
    attempt("-j abc test")

    val simple = args.parser("cat", "")
    args.positional(simple, "file", "What to show")
    args.positional(simple, "again", "What else", false)
    io.print(args.help(simple))
    val optional = args.parser("git", "Tracks files.")
    optional.commandOptional = true
    args.command(optional, "log", "Show the history")
    io.print(args.help(optional))
    ret 0
