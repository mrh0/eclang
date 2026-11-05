// core:term.ec without a terminal: styles, text width, decoding input, and what a Screen writes.
use "core:io.ec"
use "core:string.ec"
use "core:term.ec"

/** Escape sequences made readable. */
fn visible(s: String): String = replace(s, 27 as Char, '^')

/** What the first event of `bytes` is, and how many bytes it took. */
fn show(bytes: String, final: Bool = true): String do
    val d = term.decode(bytes, final)
    if val e = d.event do
        when e do
            is term.Key do
                val mods = concat(if e.ctrl do "C" else "", if e.alt do "A" else "", if e.shift do "S" else "")
                ret "${e.name as Atom} ${e.ch} ${mods} used ${d.used}"
            is term.Mouse do ret "${e.action as Atom} ${e.button as Atom} ${e.col},${e.row} used ${d.used}"
            else do ret "other"
    ret "nothing, used ${d.used}"

fn styles() do
    io.log(term.color("off", term.RED))                 // stdout isn't a terminal here
    term.setColorMode(:always)
    io.log(visible(term.color("red", term.RED)))
    io.log(visible(term.color("bright", term.BRIGHT_CYAN)))
    io.log(visible(term.color("pal", term.palette(208))))
    io.log(visible(term.color("rgb", term.rgb(1, 2, 3))))
    io.log(visible(term.bold("b")))
    io.log(visible(term.styled("x", term.Style { fg = term.WHITE, bg = term.BLUE, underline = true, reverse = true })))
    io.log(visible(term.styled("plain", term.PLAIN)))
    io.log(term.stripStyles(term.bold("clean")))
    io.log(term.displayWidth(term.bold("abc")))
    term.setColorMode(:never)
    io.log(term.bold("never"))
    term.setColorMode(:always)

fn widths() do
    io.log(term.displayWidth("héllo"))
    io.log(term.displayWidth("日本語"))
    io.log(term.charWidth(0x301))
    io.log(concat("[", term.fit("abc", 5), "]"))
    io.log(concat("[", term.fit("abcdefgh", 5), "]"))
    io.log(concat("[", term.fit("日本語", 4), "]"))

fn keys() do
    io.log(show("a"))
    io.log(show("\r"))
    io.log(show("\t"))
    io.log(show("\x7f"))
    io.log(show("\x03"))
    io.log(show("é"))
    io.log(show("日x"))
    io.log(show("\xc3", false))                         // the rest may come
    io.log(show("\xff"))                                // broken: skipped
    io.log(show("\x1b"))
    io.log(show("\x1b", false))
    io.log(show("\x1b[A"))
    io.log(show("\x1b[1;5C"))
    io.log(show("\x1b[1;2H"))
    io.log(show("\x1b[3~"))
    io.log(show("\x1b[24~"))
    io.log(show("\x1b[Z"))
    io.log(show("\x1bOP"))
    io.log(show("\x1bx"))
    io.log(show("\x1b[", false))
    io.log(show("\x1b[1;", false))
    io.log(show("\x1b[1;"))
    io.log(show("\x1b[99~"))
    io.log(show("\x1b[<0;3;4M"))
    io.log(show("\x1b[<0;3;4m"))
    io.log(show("\x1b[<32;5;6M"))
    io.log(show("\x1b[<64;1;1M"))
    io.log(show("\x1b[<65;1;1M"))
    val k = term.Key { name = :char, ch = 113 }
    io.log(term.isChar(k, 'q'))
    io.log(term.isCtrl(k, 'q'))
    io.log(term.keyText(term.Key { name = :char, ch = 0xE9 }))

fn screens() do
    val s = term.screen(12, 3)
    io.log(visible(term.frame(s)))                      // the first frame clears
    io.log(visible(term.frame(s)))                      // nothing changed
    term.text(s, 1, 0, "hi")
    io.log(visible(term.frame(s)))
    term.text(s, 1, 0, "ho", term.Style { bold = true })
    io.log(visible(term.frame(s)))                      // the h changes style too
    term.clear(s)
    term.put(s, 2, 1, 0x65E5)
    io.log(visible(term.frame(s)))                      // clears the text, then one wide character
    term.put(s, 3, 1, 'x' as UInt)                      // over its right half
    io.log(visible(term.frame(s)))
    io.log(term.text(s, 10, 2, "cut off"))              // the column after what fit
    term.put(s, 99, 99, 'x' as UInt)                    // outside: nothing
    term.put(s, 11, 0, 0x65E5)                          // a wide character at the edge
    io.log(s.cells[11].ch)
    term.placeCursor(s, 4, 2)
    io.log(visible(term.frame(s)))
    term.removeCursor(s)
    io.log(visible(term.frame(s)))

    val b = term.screen(10, 4)
    term.box(b, 0, 0, 10, 4, :rounded, "ab")
    term.text(b, 1, 1, "in")
    term.horizontalLine(b, 1, 2, 3)
    for row in 0..<4 do
        val line = stringBuilder(40)
        for col in 0..<10 do
            line.append(term.keyText(term.Key { name = :char, ch = b.cells[row * 10 + col].ch }))
        io.log(line.build())

    term.resize(s, term.Dimensions { 2, 1 })
    io.log(s.cells.len)
    io.log(visible(term.frame(s)))

fn progress() do
    io.log(term.progressBar(1, 4, 8))
    io.log(term.progressBar(9, 4, 4))
    io.log(term.progressBar(0, 0, 2))
    io.log(term.spinner(0))
    io.log(term.spinner(11))
    term.progress("skipped", 1, 2)                       // not a terminal: only the end shows
    term.progress("copying", 2, 2)

fn main(): Int do
    styles()
    widths()
    keys()
    screens()
    progress()
    ret 0
