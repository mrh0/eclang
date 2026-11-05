/**
 * Command-line arguments: flags, options, positional arguments and subcommands, with a generated --help and errors
 * that say what to do.
 *
 *     use "core:args.ec"
 *     val p = args.parser("tool", "Copies files around.")
 *     args.flag(p, "verbose", "Say what is happening", 'v')
 *     args.option(p, "output", "Where to write", 'o', "FILE", "out.txt")
 *     args.positional(p, "input", "The file to read")
 *     val r = args.run(p)                       // --help prints the help; a mistake prints why; both end the program
 *     io.log(args.isSet(r, "verbose"))
 *     io.log(args.value(r, "output") ?? "")     // what was given, or else the default
 *     io.log(args.value(r, "input") ?? "")
 *
 *     val p = args.parser("tool", "Builds things.")
 *     args.flag(p, "verbose", "Say more", 'v')
 *     val build = args.command(p, "build", "Build a target")
 *     args.positional(build, "target", "What to build")
 *     val r = args.run(p)
 *     if val sub = r.sub do                     // what follows the command
 *         io.log(r.command ?? "")               // build
 *         io.log(args.value(sub, "target") ?? "")
 *         io.log(args.isSet(sub, "verbose"))    // a command sees the options before it too
 *
 * Understood are `--name`, `--name value`, `--name=value`, `-n value`, `-nvalue`, `-abc` for several flags, and `--`,
 * after which everything is a positional argument. A word `-` and a negative number are positional arguments too,
 * unless they follow an option that takes a value. It is plain eclang: apr_getopt_long has no subcommands or help, and
 * only tells that an option was wrong, not which one, so it can't say what was meant.
 */
module "args"
use "core:errors.ec"
use "core:string.ec"
use "core:structures.ec"
use "core:io.ec"
use "core:c/stdlib.ec"

/** A flag or an option of a {@link Parser}. */
rec Spec as
    /** Its long name, without the dashes. */
    name: String
    /** What it is for, for the help. */
    help: String
    /** Its short name, 0 when it has none. */
    short: Char
    /** Whether it takes a value: an option does, a flag doesn't. */
    takesValue: Bool
    /** What the help calls its value. */
    valueName: String
    /** Its value when it isn't given. */
    fallback: String?
    /** Whether it must be given. */
    required: Bool

/** A positional argument of a {@link Parser}. */
rec Positional as
    /** Its name. */
    name: String
    /** What it is for, for the help. */
    help: String
    /** Whether it must be given. */
    required: Bool
    /** Whether it takes all the words that are left. */
    repeated: Bool

/** What a program or command accepts, from {@link parser} and {@link command}. */
rec Parser as
    /** Its name. */
    name: String
    /** The whole way to call it, for the help: "tool build". */
    path: String
    /** What it does, for the help. */
    about: String
    /** Its flags and options. */
    options: @List<@Spec>
    /** Its positional arguments. */
    positionals: @List<@Positional>
    /** Its commands. */
    commands: @List<@Parser>
    /** Whether it can be called without a command when it has commands. */
    commandOptional: Bool

/** What the command line said, from {@link parse} and {@link run}. */
rec Parsed as
    /** What it was parsed with. */
    parser: @Parser
    /** The words of each option and positional argument, by name; a flag has one for each time it was given. */
    given: @Map<String, @List<String>>
    /** The parts of the program this is a command of. */
    parent: @Parsed?
    /** The name of the command that was given, or null. */
    command: String?
    /** What follows the command, or null. */
    sub: @Parsed?
    /** The help to print, when `--help` was given: nothing else is checked then. */
    help: String?

/** The command line is wrong. Its message says how; {@link run} prints it. */
rec ArgsError extends ArgumentError as
    /** How to get the help, for the line after the message: "Try 'tool --help'.". */
    hint: String

// ---------------------------------------------------------------------------------------------------
// Saying what is accepted

/** A new {@link Parser}, in the current pool. */
priv fn newParser(name: String, path: String, about: String): @Parser do
    val options: @List<@Spec> = list()
    val positionals: @List<@Positional> = list()
    val commands: @List<@Parser> = list()
    ret new Parser { name, path, about, options, positionals, commands, false }

/**
 * A new {@link Parser} for a program.
 * @param name the program's name, for the help
 * @param about what it does, for the help
 */
fn parser(name: String, about: String): @Parser = newParser(name, name, about)

/**
 * Adds a flag: `--name`, which is on or off.
 * @param p the parser
 * @param name its long name, without the dashes
 * @param help what it is for
 * @param short its short name; none by default
 */
fn flag(p: @Parser, name: String, help: String, short: Char = 0 as Char) do
    p.options.push(new Spec { name, help, short, false, "", null, false })

/**
 * Adds an option: `--name value`, which may be left out. Given several times, it has several values; see {@link valuesOf}.
 * @param p the parser
 * @param name its long name, without the dashes
 * @param help what it is for
 * @param short its short name; none by default
 * @param valueName what the help calls its value
 */
fn option(p: @Parser, name: String, help: String, short: Char = 0 as Char, valueName: String = "VALUE") do
    p.options.push(new Spec { name, help, short, true, valueName, null, false })

/**
 * Adds an option with a value for when it isn't given.
 * @param p the parser
 * @param name its long name, without the dashes
 * @param help what it is for
 * @param short its short name, 0 for none
 * @param valueName what the help calls its value
 * @param fallback its value when it isn't given
 */
fn option(p: @Parser, name: String, help: String, short: Char, valueName: String, fallback: String) do
    p.options.push(new Spec { name, help, short, true, valueName, fallback, false })

/**
 * Adds an option that must be given.
 * @param p the parser
 * @param name its long name, without the dashes
 * @param help what it is for
 * @param short its short name; none by default
 * @param valueName what the help calls its value
 */
fn requiredOption(p: @Parser, name: String, help: String, short: Char = 0 as Char, valueName: String = "VALUE") do
    p.options.push(new Spec { name, help, short, true, valueName, null, true })

/**
 * Adds a positional argument, the next word that isn't an option. They are filled in the order they are added.
 * @param p the parser
 * @param name its name, also for reading it
 * @param help what it is for
 * @param required whether it must be given; those that may be left out come last
 */
fn positional(p: @Parser, name: String, help: String, required: Bool = true) do
    p.positionals.push(new Positional { name, help, required, false })

/**
 * Adds a last positional argument that takes all the words that are left; see {@link valuesOf}.
 * @param p the parser
 * @param name its name, also for reading it
 * @param help what it is for
 * @param required whether at least one must be given
 */
fn rest(p: @Parser, name: String, help: String, required: Bool = false) do
    p.positionals.push(new Positional { name, help, required, true })

/**
 * Adds a command, as in `git commit`, with options of its own: the {@link Parser} it returns. A program with commands
 * must be given one, unless its `commandOptional` is set.
 * @param p the parser
 * @param name what is typed
 * @param about what it does, for the help
 */
fn command(p: @Parser, name: String, about: String): @Parser do
    val sub = newParser(name, "${p.path} ${name}", about)
    p.commands.push(sub)
    ret sub

// ---------------------------------------------------------------------------------------------------
// Help

/** `--name` with its short form and value, as the help shows it. */
priv fn optionLabel(o: @Spec): String do
    val short = if o.short != (0 as Char) do "-${o.short}, " else "    "
    val value = if o.takesValue do " ${o.valueName}" else ""
    ret "${short}--${o.name}${value}"

/** Adds one line of a table: `label` padded to `width`, then `text`. */
priv fn row(out: @StringBuilder, label: String, width: Size, text: String) do
    out.append("  ")
    out.append(padEnd(label, width + 2))
    out.appendLine(text)

/** The line of the help that tells how to call `p`. */
priv fn usageLine(p: @Parser): String do
    val out = stringBuilder(64)
    out.append("Usage: ")
    out.append(p.path)
    out.append(" [options]")
    for a in p.positionals do
        val dots = if a.repeated do "..." else ""
        out.append(if a.required do " <${a.name}>${dots}" else " [${a.name}]${dots}")
    if p.commands.len > 0 do
        out.append(if p.commandOptional do " [command]" else " <command>")
    ret out.build()

/** The help of `p`, as `--help` prints it. */
fn help(p: @Parser): String do
    val out = stringBuilder(512)
    if p.about.len > 0 do
        out.appendLine(p.about)
        out.append('\n')
    out.appendLine(usageLine(p))
    if p.positionals.len > 0 do
        var width: Size = 0
        for a in p.positionals where a.name.len > width do
            width = a.name.len
        out.append("\nArguments:\n")
        for a in p.positionals do
            row(out, a.name, width, a.help)
    var width = "-h, --help".len
    for o in p.options where optionLabel(o).len > width do
        width = optionLabel(o).len
    out.append("\nOptions:\n")
    for o in p.options do
        var text = o.help
        if val value = o.fallback do
            text = "${text} (default: ${value})"
        row(out, optionLabel(o), width, if o.required do "${text} (required)" else text)
    row(out, "-h, --help", width, "Show this help")
    if p.commands.len > 0 do
        var commandWidth: Size = 0
        for c in p.commands where c.name.len > commandWidth do
            commandWidth = c.name.len
        out.append("\nCommands:\n")
        for c in p.commands do
            row(out, c.name, commandWidth, c.about)
        out.append("\nRun '${p.path} <command> --help' for the options of a command.\n")
    ret out.build()

// ---------------------------------------------------------------------------------------------------
// Parsing

/** The {@link ArgsError} of `p`. */
priv fn wrong(p: @Parser, argument: String, message: String): ArgsError = ArgsError { ERR_ARGUMENT, message, argument, "Try '${p.path} --help'." }

/** How many single edits turn `a` into `b` (the Levenshtein distance). */
priv fn distance(a: String, b: String): Size do
    var previous = new Size[b.len + 1]
    var current = new Size[b.len + 1]
    for j in 0..=b.len do
        previous[j] = j
    for i in 1..=a.len do
        current[0] = i
        for j in 1..=b.len do
            val cost: Size = if a[i - 1] == b[j - 1] do 0 else 1
            val replaced = previous[j - 1] + cost
            val removed = previous[j] + 1
            val added = current[j - 1] + 1
            var best = replaced
            if removed < best do
                best = removed
            if added < best do
                best = added
            current[j] = best
        val swap = previous
        previous = current
        current = swap
    ret previous[b.len]

/** The one of `names` closest to `word`, or null when none is close enough to be a typing mistake. */
priv fn closest(word: String, names: @List<String>): String? do
    var best: String? = null
    var bestDistance = word.len / 2 + 1
    if bestDistance > 3 do
        bestDistance = 3
    bestDistance += 1
    for name in names do
        val d = distance(word, name)
        if d < bestDistance do
            bestDistance = d
            best = name
    ret best

/** " Did you mean 'x'?" for `word` among `names`, or "". */
priv fn suggestion(word: String, names: @List<String>, prefix: String): String do
    if val name = closest(word, names) do
        ret " Did you mean '${prefix}${name}'?"
    ret ""

/** The long option `name` of `p`, or null. */
priv fn longOption(p: @Parser, name: String): @Spec? do
    for o in p.options where o.name == name do
        ret o
    ret null

/** The short option `c` of `p`, or null. */
priv fn shortOption(p: @Parser, c: Char): @Spec? do
    for o in p.options where o.short == c do
        ret o
    ret null

/** Remembers that `name` was given, with `word`. */
priv fn remember(r: @Parsed, name: String, word: String) do
    if val words = r.given.get(name) do
        words.push(word)
    else do
        val words: @List<String> = list()
        words.push(word)
        r.given.put(name, words)

/** Whether `word` looks like a number, which isn't an option: "-1", "-2.5". */
priv fn isNegativeNumber(word: String): Bool = word.len > 1 and word[0] == '-' and (isDigit(word[1]) or word[1] == '.')

/** What is left to read of the words. */
priv rec Words as
    /** All the words. */
    all: String[]
    /** The next one. */
    at: Size

/**
 * The next word, as the value of `shown`.
 * @throws when there is none
 */
priv fn valueOf(p: @Parser, w: @Words, shown: String, o: @Spec): String throws ArgsError do
    throw wrong(p, shown, "the option '${shown}' needs a value: ${shown} ${o.valueName}") if w.at >= w.all.len
    val word = w.all[w.at]
    w.at += 1
    ret word

/**
 * Reads the word `--...`.
 * @throws when it isn't an option of `p`, or its value is wrong
 */
priv fn readLongOption(p: @Parser, r: @Parsed, w: @Words, word: String) throws ArgsError do
    val equals = indexOf(word, '=')
    val name = word[2..<(equals ?? word.len)]
    val shown = "--${name}"
    if val o = longOption(p, name) do
        if val at = equals do
            throw wrong(p, shown, "the flag '${shown}' doesn't take a value; remove '=${word[at + 1..<word.len]}'") if not o.takesValue
            remember(r, name, word[at + 1..<word.len])
        else do
            if o.takesValue do
                remember(r, name, try valueOf(p, w, shown, o))
            else do
                remember(r, name, "")
    else do
        val names: @List<String> = list()
        for o in p.options do
            names.push(o.name)
        names.push("help")
        val hint = suggestion(name, names, "--")
        throw wrong(p, shown, "unknown option '${shown}'.${hint}")

/**
 * Reads the word `-abc`.
 * @throws when something in it isn't an option of `p`, or a value is missing
 */
priv fn readShortOptions(p: @Parser, r: @Parsed, w: @Words, word: String) throws ArgsError do
    var i: Size = 1
    while i < word.len do
        val c = word[i]
        val shown = "-${c}"
        i += 1
        if val o = shortOption(p, c) do
            if o.takesValue do
                if i < word.len do
                    val attached = removePrefix(word[i..<word.len], "=")
                    remember(r, o.name, attached)
                    i = word.len
                else do
                    remember(r, o.name, try valueOf(p, w, shown, o))
            else do
                remember(r, o.name, "")
        else do
            throw wrong(p, shown, "unknown option '${shown}'.")

/**
 * Checks that what must be given was given.
 * @throws when something is missing
 */
priv fn checkRequired(p: @Parser, r: @Parsed) throws ArgsError do
    for o in p.options where o.required and r.given.get(o.name) == null do
        throw wrong(p, "--${o.name}", "the option '--${o.name}' is required: --${o.name} ${o.valueName}")
    for a in p.positionals where a.required and r.given.get(a.name) == null do
        throw wrong(p, a.name, "the argument <${a.name}> is missing. ${usageLine(p)}")
    if p.commands.len > 0 and r.command == null and not p.commandOptional do
        val names: @List<String> = list()
        for c in p.commands do
            names.push(c.name)
        val all = join(names.items(), ", ")
        throw wrong(p, "command", "a command is missing: ${all}")

/** Whether the word `word` asks for the help. */
priv fn isHelp(word: String): Bool = word == "--help" or word == "-h"

/**
 * Reads the words from `w` on as `p` says.
 * @throws when they are wrong
 */
priv fn parseWords(p: @Parser, w: @Words, parent: @Parsed?): @Parsed throws ArgsError do
    val given: @Map<String, @List<String>> = map()
    val r = new Parsed { p, given, parent, null, null, null }
    var position: Size = 0
    var stopped = false
    while w.at < w.all.len do
        val word = w.all[w.at]
        w.at += 1
        if not stopped and word == "--" do
            stopped = true
        else if not stopped and isHelp(word) do
            r.help = help(p)
            ret r
        else if not stopped and startsWith(word, "--") do
            try readLongOption(p, r, w, word)
        else if not stopped and startsWith(word, '-') and word.len > 1 and not isNegativeNumber(word) do
            try readShortOptions(p, r, w, word)
        else if p.commands.len > 0 and position == p.positionals.len do
            var found: @Parser? = null
            val names: @List<String> = list()
            for c in p.commands do
                names.push(c.name)
                if c.name == word do
                    found = c
            if found == null do
                val hint = suggestion(word, names, "")
                val all = join(names.items(), ", ")
                throw wrong(p, word, "unknown command '${word}'.${hint} The commands are: ${all}")
            val sub = try parseWords(found, w, r)
            r.command = word
            r.sub = sub
            r.help = sub.help
            ret r if sub.help != null
        else if position < p.positionals.len do
            val a = p.positionals.buffer[position]
            remember(r, a.name, word)
            if not a.repeated do
                position += 1
        else do
            throw wrong(p, word, "unexpected argument '${word}'. ${usageLine(p)}")
    try checkRequired(p, r)
    ret r

/**
 * Reads command-line words as `p` says. When the help is asked for, nothing else is checked, and `help` in what comes
 * back holds it. See {@link run} for what most programs need.
 * @param p the parser
 * @param words the words, usually `io.args()`
 * @throws when they are wrong; the message says how
 */
fn parse(p: @Parser, words: String[]): @Parsed throws ArgsError do
    val w = new Words { words, 0 }
    ret try parseWords(p, w, null)

/** Where {@link run} gets the result of {@link parse}: a `catch` can't give a value. */
priv rec Outcome as
    /** What was parsed. */
    parsed: @Parsed?

/**
 * Keeps the result of {@link parse} in `out`.
 * @throws when the words are wrong
 */
priv fn parseInto(out: @Outcome, p: @Parser, words: String[]) throws ArgsError do
    out.parsed = try parse(p, words)

/**
 * Reads the program's arguments as `p` says. With `--help` this prints the help and ends the program with exit code 0.
 * When they are wrong, it prints why to stderr and ends the program with exit code 2.
 */
fn run(p: @Parser): @Parsed do
    val out = new Outcome { null }
    try parseInto(out, p, io.args()) catch problem do
        io.flush()
        io.eprintln("${p.name}: ${problem.message}")
        io.eprintln(problem.hint)
        exit(2)
    val r = out.parsed!
    if val text = r.help do
        io.print(text)
        io.flush()
        exit(0)
    ret r

// ---------------------------------------------------------------------------------------------------
// Reading the result

/** The words of `name`, here or in the program a command belongs to, or null when it wasn't given. */
priv fn wordsOf(r: @Parsed, name: String): @List<String>? do
    if val words = r.given.get(name) do
        ret words
    if val up = r.parent do
        ret wordsOf(up, name)
    ret null

/** The default of the option `name`, here or in the program a command belongs to. */
priv fn fallbackOf(r: @Parsed, name: String): String? do
    if val o = longOption(r.parser, name) do
        ret o.fallback
    if val up = r.parent do
        ret fallbackOf(up, name)
    ret null

/** Whether a flag, option or positional argument was given. */
fn isSet(r: @Parsed, name: String): Bool = wordsOf(r, name) != null

/** How many times it was given: for `-vvv`, 3. */
fn times(r: @Parsed, name: String): Size do
    if val words = wordsOf(r, name) do
        ret words.len
    ret 0

/** The value of an option or positional argument: the last one given, or else the default, or else null. */
fn value(r: @Parsed, name: String): String? do
    if val words = wordsOf(r, name) do
        ret words.buffer[words.len - 1]
    ret fallbackOf(r, name)

/** All the values of an option or positional argument, in the order they were given; the default when none was. */
fn valuesOf(r: @Parsed, name: String): String[] do
    if val words = wordsOf(r, name) do
        ret words.items()
    val out: @List<String> = list()
    if val fallback = fallbackOf(r, name) do
        out.push(fallback)
    ret out.items()

/**
 * The value of an option or positional argument as a whole number.
 * @return the number, or null when it has no value
 * @throws when it isn't a whole number
 */
fn longValue(r: @Parsed, name: String): Long? throws ArgsError do
    if val text = value(r, name) do
        try parseInt(text) catch problem do
            throw wrong(r.parser, name, "'${name}' must be a whole number, not '${text}'")
        ret parseInt(text) ?? 0
    ret null
