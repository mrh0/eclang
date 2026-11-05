# Module `args`

Command-line arguments: flags, options, positional arguments and subcommands, with a generated --help and errors
that say what to do.

    use "core:args.ec"
    val p = args.parser("tool", "Copies files around.")
    args.flag(p, "verbose", "Say what is happening", 'v')
    args.option(p, "output", "Where to write", 'o', "FILE", "out.txt")
    args.positional(p, "input", "The file to read")
    val r = args.run(p)                       // --help prints the help; a mistake prints why; both end the program
    io.log(args.isSet(r, "verbose"))
    io.log(args.value(r, "output") ?? "")     // what was given, or else the default
    io.log(args.value(r, "input") ?? "")

    val p = args.parser("tool", "Builds things.")
    args.flag(p, "verbose", "Say more", 'v')
    val build = args.command(p, "build", "Build a target")
    args.positional(build, "target", "What to build")
    val r = args.run(p)
    if val sub = r.sub do                     // what follows the command
        io.log(r.command ?? "")               // build
        io.log(args.value(sub, "target") ?? "")
        io.log(args.isSet(sub, "verbose"))    // a command sees the options before it too

Understood are `--name`, `--name value`, `--name=value`, `-n value`, `-nvalue`, `-abc` for several flags, and `--`,
after which everything is a positional argument. A word `-` and a negative number are positional arguments too,
unless they follow an option that takes a value. It is plain eclang: apr_getopt_long has no subcommands or help, and
only tells that an option was wrong, not which one, so it can't say what was meant.

## Types

### <a id="args.Spec"></a>`Spec`

```ec
rec Spec
```

A flag or an option of a [`Parser`](#args.Parser).

**Fields**

- <a id="args.Spec.name"></a>`name: String` — Its long name, without the dashes.
- <a id="args.Spec.help"></a>`help: String` — What it is for, for the help.
- <a id="args.Spec.short"></a>`short: Char` — Its short name, 0 when it has none.
- <a id="args.Spec.takesValue"></a>`takesValue: Bool` — Whether it takes a value: an option does, a flag doesn't.
- <a id="args.Spec.valueName"></a>`valueName: String` — What the help calls its value.
- <a id="args.Spec.fallback"></a>`fallback: String?` — Its value when it isn't given.
- <a id="args.Spec.required"></a>`required: Bool` — Whether it must be given.

### <a id="args.Positional"></a>`Positional`

```ec
rec Positional
```

A positional argument of a [`Parser`](#args.Parser).

**Fields**

- <a id="args.Positional.name"></a>`name: String` — Its name.
- <a id="args.Positional.help"></a>`help: String` — What it is for, for the help.
- <a id="args.Positional.required"></a>`required: Bool` — Whether it must be given.
- <a id="args.Positional.repeated"></a>`repeated: Bool` — Whether it takes all the words that are left.

### <a id="args.Parser"></a>`Parser`

```ec
rec Parser
```

What a program or command accepts, from [`parser`](#args.parser) and [`command`](#args.command).

**Fields**

- <a id="args.Parser.name"></a>`name: String` — Its name.
- <a id="args.Parser.path"></a>`path: String` — The whole way to call it, for the help: "tool build".
- <a id="args.Parser.about"></a>`about: String` — What it does, for the help.
- <a id="args.Parser.options"></a>`options: @List<@Spec>` — Its flags and options.
- <a id="args.Parser.positionals"></a>`positionals: @List<@Positional>` — Its positional arguments.
- <a id="args.Parser.commands"></a>`commands: @List<@Parser>` — Its commands.
- <a id="args.Parser.commandOptional"></a>`commandOptional: Bool` — Whether it can be called without a command when it has commands.

### <a id="args.Parsed"></a>`Parsed`

```ec
rec Parsed
```

What the command line said, from [`parse`](#args.parse) and [`run`](#args.run).

**Fields**

- <a id="args.Parsed.parser"></a>`parser: @Parser` — What it was parsed with.
- <a id="args.Parsed.given"></a>`given: @Map<String, @List<String>>` — The words of each option and positional argument, by name; a flag has one for each time it was given.
- <a id="args.Parsed.parent"></a>`parent: @Parsed?` — The parts of the program this is a command of.
- <a id="args.Parsed.command"></a>`command: String?` — The name of the command that was given, or null.
- <a id="args.Parsed.sub"></a>`sub: @Parsed?` — What follows the command, or null.
- <a id="args.Parsed.help"></a>`help: String?` — The help to print, when `--help` was given: nothing else is checked then.

### <a id="args.ArgsError"></a>`ArgsError`

```ec
rec ArgsError extends ArgumentError
```

The command line is wrong. Its message says how; [`run`](#args.run) prints it.

**Fields**

- <a id="args.ArgsError.hint"></a>`hint: String` — How to get the help, for the line after the message: "Try 'tool --help'.".

## Functions

### <a id="args.parser"></a>`parser`

```ec
fn parser(name: String, about: String): @Parser
```

A new [`Parser`](#args.Parser) for a program.

**Parameters**

- `name` — the program's name, for the help
- `about` — what it does, for the help

### <a id="args.flag"></a>`flag`

```ec
fn flag(p: @Parser, name: String, help: String, short: Char = 0 as Char)
```

Adds a flag: `--name`, which is on or off.

**Parameters**

- `p` — the parser
- `name` — its long name, without the dashes
- `help` — what it is for
- `short` — its short name; none by default

### <a id="args.option"></a>`option`

```ec
fn option(p: @Parser, name: String, help: String, short: Char = 0 as Char, valueName: String = "VALUE")
```

Adds an option: `--name value`, which may be left out. Given several times, it has several values; see [`valuesOf`](#args.valuesOf).

**Parameters**

- `p` — the parser
- `name` — its long name, without the dashes
- `help` — what it is for
- `short` — its short name; none by default
- `valueName` — what the help calls its value

### `option`

```ec
fn option(p: @Parser, name: String, help: String, short: Char, valueName: String, fallback: String)
```

Adds an option with a value for when it isn't given.

**Parameters**

- `p` — the parser
- `name` — its long name, without the dashes
- `help` — what it is for
- `short` — its short name, 0 for none
- `valueName` — what the help calls its value
- `fallback` — its value when it isn't given

### <a id="args.requiredOption"></a>`requiredOption`

```ec
fn requiredOption(p: @Parser, name: String, help: String, short: Char = 0 as Char, valueName: String = "VALUE")
```

Adds an option that must be given.

**Parameters**

- `p` — the parser
- `name` — its long name, without the dashes
- `help` — what it is for
- `short` — its short name; none by default
- `valueName` — what the help calls its value

### <a id="args.positional"></a>`positional`

```ec
fn positional(p: @Parser, name: String, help: String, required: Bool = true)
```

Adds a positional argument, the next word that isn't an option. They are filled in the order they are added.

**Parameters**

- `p` — the parser
- `name` — its name, also for reading it
- `help` — what it is for
- `required` — whether it must be given; those that may be left out come last

### <a id="args.rest"></a>`rest`

```ec
fn rest(p: @Parser, name: String, help: String, required: Bool = false)
```

Adds a last positional argument that takes all the words that are left; see [`valuesOf`](#args.valuesOf).

**Parameters**

- `p` — the parser
- `name` — its name, also for reading it
- `help` — what it is for
- `required` — whether at least one must be given

### <a id="args.command"></a>`command`

```ec
fn command(p: @Parser, name: String, about: String): @Parser
```

Adds a command, as in `git commit`, with options of its own: the [`Parser`](#args.Parser) it returns. A program with commands
must be given one, unless its `commandOptional` is set.

**Parameters**

- `p` — the parser
- `name` — what is typed
- `about` — what it does, for the help

### <a id="args.help"></a>`help`

```ec
fn help(p: @Parser): String
```

The help of `p`, as `--help` prints it.

### <a id="args.parse"></a>`parse`

```ec
fn parse(p: @Parser, words: String[]): @Parsed throws ArgsError
```

Reads command-line words as `p` says. When the help is asked for, nothing else is checked, and `help` in what comes
back holds it. See [`run`](#args.run) for what most programs need.

**Parameters**

- `p` — the parser
- `words` — the words, usually `io.args()`

**Throws**

- `ArgsError` — when they are wrong; the message says how

### <a id="args.run"></a>`run`

```ec
fn run(p: @Parser): @Parsed
```

Reads the program's arguments as `p` says. With `--help` this prints the help and ends the program with exit code 0.
When they are wrong, it prints why to stderr and ends the program with exit code 2.

### <a id="args.isSet"></a>`isSet`

```ec
fn isSet(r: @Parsed, name: String): Bool
```

Whether a flag, option or positional argument was given.

### <a id="args.times"></a>`times`

```ec
fn times(r: @Parsed, name: String): Size
```

How many times it was given: for `-vvv`, 3.

### <a id="args.value"></a>`value`

```ec
fn value(r: @Parsed, name: String): String?
```

The value of an option or positional argument: the last one given, or else the default, or else null.

### <a id="args.valuesOf"></a>`valuesOf`

```ec
fn valuesOf(r: @Parsed, name: String): String[]
```

All the values of an option or positional argument, in the order they were given; the default when none was.

### <a id="args.longValue"></a>`longValue`

```ec
fn longValue(r: @Parsed, name: String): Long? throws ArgsError
```

The value of an option or positional argument as a whole number.

**Returns** the number, or null when it has no value

**Throws**

- `ArgsError` — when it isn't a whole number
