// A small full-screen program: pick a task with the arrow keys or the mouse, and mark it done with space.
// Run it in a terminal: java -jar target/eclang-0.1.0.jar run examples/tui.ec
use "core:io.ec"
use "core:term.ec"

rec Task as
    name: String
    done: Bool = false

fn draw(s: @term.Screen, tasks: Task[], current: Size) do
    term.clear(s)
    term.box(s, 0, 0, s.cols, s.rows, :rounded, "Tasks", term.Style { fg = term.CYAN })
    for i in 0..<tasks.len do
        val mark = if tasks[i].done do "[x] " else "[ ] "
        val look = if i == current do term.Style { reverse = true } else term.PLAIN
        val row = (i as Int) + 2
        val after = term.text(s, 3, row, mark, look)
        term.text(s, after, row, tasks[i].name, if tasks[i].done do term.Style { dim = true, strike = true } else look)
    term.text(s, 3, s.rows - 2, "up/down  space: done  q: quit", term.Style { fg = term.GRAY })
    term.render(s)

fn main(): Int throws Error do
    val tasks = [Task { "Read the README" }, Task { "Write a program" }, Task { "Ship it" }]
    var current: Size = 0
    try term.enterFullscreen()
    defer term.leaveFullscreen()
    term.enableMouse()
    val s = term.screen()
    while true do
        draw(s, tasks, current)
        val e = term.readEvent()
        when e do
            is term.Key do
                break if term.isChar(e, 'q') or e.name == :escape or e.name == :char and term.isCtrl(e, 'c')
                if e.name == :up and current > 0 do
                    current -= 1
                else if e.name == :down and current + 1 < tasks.len do
                    current += 1
                else if term.isChar(e, ' ') do
                    tasks[current] = { ...tasks[current], done = not tasks[current].done }
            is term.Mouse do
                val i = e.row - 2
                if e.action == :press and i >= 0 and (i as Size) < tasks.len do
                    current = i as Size
            is term.Resize do term.resize(s, e.size)
            is term.Eof do break
    ret 0
