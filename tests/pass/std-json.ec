use "core:io.ec"
use "core:json.ec"
use "core:string.ec"

fn show(text: String) do
    try echo(text) catch problem do
        io.log(problem.message)

fn echo(text: String) throws json.JsonError do
    io.log(json.stringify(try json.parse(text)))

fn main(): Int throws Error do
    val v = try json.parse(" {\"name\": \"Ada\", \"langs\": [\"c\", \"ec\"], \"age\": 36, \"pi\": 3.25, \"on\": true, \"none\": null, \"nested\": {\"list\": [{\"x\": 7}]}} ")
    io.log(json.asString(json.field(v, "name")) ?? "?")
    io.log(json.asLong(json.find(v, "age")) ?? 0)
    io.log(json.asNumber(json.field(v, "pi")) ?? 0.0)
    io.log(json.asBool(json.field(v, "on")) ?? false)
    io.log(json.isNull(json.field(v, "none")))
    io.log(json.isNull(json.field(v, "missing")))
    io.log(json.asString(json.find(v, "langs[1]")) ?? "?")
    io.log(json.asLong(json.find(v, "nested.list[0].x")) ?? 0)
    io.log(json.find(v, "nested.list[5].x") == null)
    io.log(json.find(v, "langs[x]") == null)
    io.log(json.asString(json.field(json.field(v, "nope"), "deeper")) ?? "null all the way")
    io.log(json.asLong(json.field(v, "pi")) == null)
    io.log(json.count(v))
    io.log(json.count(json.field(v, "langs")))
    for key in json.keys(v) do
        io.print("${key} ")
    io.println("")
    for lang in json.items(json.field(v, "langs")) do
        io.log(json.asString(lang) ?? "?")

    val out = json.object()
    json.put(out, "ok", true)
    json.put(out, "count", 3)
    json.put(out, "ratio", 0.1)
    json.put(out, "text", "a\"b\\c\n\t\x01é")
    val list = json.array()
    json.add(list, "a")
    json.add(list, 1)
    json.add(list, 2.5)
    json.add(list, false)
    json.add(list, json.nil())
    json.add(list, json.object())
    json.add(list, json.array())
    json.put(out, "list", list)
    json.put(out, "count", 4)
    io.log(json.stringify(out))
    io.log(json.pretty(out))
    io.log(json.pretty(out, 4) == json.pretty(try json.parse(json.pretty(out)), 4))

    // Numbers stay as they were.
    show("[0, -0, 9223372036854775807, -9223372036854775807, 9223372036854775808, 0.1, 1e3, 1.5e300, 3.0, 1E-2]")
    show("\"\\u00e9 \\ud83d\\ude00 \\/ \\b\\f\"")
    show("{\"a\": 1, \"a\": 2}")
    var zero = 0.0
    io.log(json.stringify(json.number(1.0 / zero)))

    // What is wrong, and where.
    show("")
    show("  ")
    show("[1, 2")
    show("[1, 2,]")
    show("[1, 2,  ]")
    show("{\"a\": 1,\n  }")
    show("[1 2]")
    show("{\"a\": 1,}")
    show("{\"a\" 1}")
    show("{a: 1}")
    show("{\"a\": 1")
    show("{'a': 1}")
    show("['a']")
    show("[1,\n 2,\n  tru]")
    show("// nothing\n1")
    show("01")
    show("-")
    show("1.")
    show("1e")
    show("1e999")
    show("true false")
    show("\"abc")
    show("\"a\nb\"")
    show("\"\\x\"")
    show("\"\\u12\"")
    show("\"\\ud83d\"")
    show("\"\\ude00\"")
    show("\"\xff\"")
    show(repeat("[", 600))
    try json.parse("[1,\n  x]") catch problem do
        io.log("${problem.line} ${problem.column} ${problem.position}")
    ret 0
