// Run by tests/term-pty.py inside a pseudo-terminal: each argument picks what to do.
use "core:io.ec"
use "core:string.ec"
use "core:term.ec"

/** Writes to the alternate screen's stream in a form the test can find. */
fn say(s: String) do
    io.print("<${s}>")
    io.flush()

/** Reports events until q, or the end of the input. */
fn events() do
    while true do
        pool do
            val e = term.readEvent()
            when e do
                is term.Key do
                    if term.isChar(e, 'q') do
                        say("quit")
                        term.leaveFullscreen()
                        exit(0)
                    say("key ${e.name as Atom} ${e.ch} ${e.ctrl} ${e.alt}")
                is term.Mouse do say("mouse ${e.action as Atom} ${e.col} ${e.row}")
                is term.Resize do say("resize ${e.size.cols} ${e.size.rows}")
                is term.Eof do
                    say("eof")
                    term.leaveFullscreen()
                    exit(0)

fn main(): Int throws Error do
    val mode = io.args()[0]
    if mode == "events" do
        try term.enterFullscreen()
        term.enableMouse()
        say("ready")
        events()
    else if mode == "keysonly" do
        try term.enterFullscreen(false)
        say("ready")
        events()
    else if mode == "panic" do
        try term.enterFullscreen()
        say("ready")
        val none = new Int[0]
        io.log(none[3])
    else if mode == "exit" do
        try term.enterFullscreen()
        say("ready")
        io.eprintln("held back")
        exit(3)
    else if mode == "return" do
        try term.enterRaw()
        say("ready")
        ret 4
    else if mode == "cursor" do
        term.hideCursor()
        say("ready")
        term.readEvent()
    else if mode == "timeout" do
        try term.enterRaw()
        say("ready")
        val e = term.pollEvent(50000)
        say(if e == null do "timed out" else "got something")
        term.leaveRaw()
    else if mode == "prompts" do
        val name = term.ask("name: ") ?? "<none>"
        val secret = term.password("secret: ") ?? "<none>"
        val yes = term.confirm("sure?")
        val i = term.select("pick", ["one", "two", "three"])
        say("${name}|${secret}|${yes}|${i ?? 99}")
    else if mode == "screen" do
        try term.enterFullscreen()
        val s = term.screen()
        term.box(s, 0, 0, s.cols, s.rows, :single, "t")
        term.render(s)
        say("size ${s.cols} ${s.rows}")
        term.text(s, 1, 1, "z")
        term.render(s)
        say("done")
        term.leaveFullscreen()
    ret 0
