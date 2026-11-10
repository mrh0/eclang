# Module `term`

Terminal tools: colours and styles, the cursor, key and mouse input, a screen buffer to draw on, and prompts.
Waiting, reading and writing use APR (apr_poll, apr_file_*, apr_signal); the terminal's own settings use termios,
which APR doesn't cover, so this file needs a POSIX system.

    use "core:term.ec"
    io.log(term.color("done", term.GREEN))             // plain text when stdout isn't a terminal
    val ok = term.confirm("Delete it?")
    val pick = term.select("Colour", ["red", "green"]) // arrow keys and Enter; null for Esc

A full-screen program draws on a [`Screen`](#term.Screen) and reacts to events:

    try term.enterFullscreen()
    defer term.leaveFullscreen()
    val s = term.screen()
    while true do
        term.clear(s)
        term.box(s, 0, 0, s.cols, s.rows, :rounded, "Demo")
        term.text(s, 2, 1, "press q", term.Style { bold = true })
        term.render(s)                                 // writes only the cells that changed
        val e = term.readEvent()
        when e do
            is term.Key do break if term.isChar(e, 'q')
            is term.Resize do term.resize(s, e.size)
            is term.Mouse do continue
            is term.Eof do break

Columns and rows count from 0, at the top left. A loop that allocates should have a `pool do` block in its body.

The terminal is put back when the program ends, also after a panic or a fatal signal. In full-screen mode stderr
is held back, so it doesn't scribble on the screen, and comes out when the mode ends: a panic's message stays
readable. Ctrl-Z works too: the terminal is put back while the program is stopped, and a [`Resize`](#term.Resize) event
after it continues says to draw again.

Input is read past C's stdio, so don't use `io.readLine` and these functions on the same piped input: stdio
reads ahead. Output can be mixed with `io.print`.

## Types

### <a id="term.Dimensions"></a>`Dimensions`

```ec
rec Dimensions
```

A size in character cells.

**Fields**

- <a id="term.Dimensions.cols"></a>`cols: Int` — Columns: the width.
- <a id="term.Dimensions.rows"></a>`rows: Int` — Rows: the height.

### <a id="term.Color"></a>`Color`

```ec
rec Color
```

A colour. Use the constants, [`rgb`](#term.rgb) or [`palette`](#term.palette).

**Fields**

- <a id="term.Color.code"></a>`code: Int = 0` — 0: the terminal's default; 1 to 256: a palette index plus 1; from RGB_FLAG: red, green and blue, 8 bits each.

### <a id="term.Style"></a>`Style`

```ec
rec Style
```

How text looks. Name what you want: `Style { fg = term.RED, bold = true }`.

**Fields**

- <a id="term.Style.fg"></a>`fg: Color = Color { 0 }` — The colour of the text.
- <a id="term.Style.bg"></a>`bg: Color = Color { 0 }` — The colour behind the text.
- <a id="term.Style.bold"></a>`bold: Bool = false` — Thick text.
- <a id="term.Style.dim"></a>`dim: Bool = false` — Faint text.
- <a id="term.Style.italic"></a>`italic: Bool = false` — Slanted text.
- <a id="term.Style.underline"></a>`underline: Bool = false` — Underlined text.
- <a id="term.Style.reverse"></a>`reverse: Bool = false` — Text and background colours swapped.
- <a id="term.Style.strike"></a>`strike: Bool = false` — Crossed-out text.

### <a id="term.ColorMode"></a>`ColorMode`

```ec
declare type ColorMode as :auto | :always | :never
```

When [`styled`](#term.styled) and the functions built on it add colours:

- `:auto`: when stdout is a terminal and the NO_COLOR environment variable and TERM=dumb don't say otherwise
- `:always`: always, for output that goes to a pager, say
- `:never`: never

### <a id="term.KeyName"></a>`KeyName`

```ec
declare type KeyName as :char | :enter | :tab | :backspace | :escape | :up | :down | :left | :right | :home | :end | :pageUp | :pageDown | :insert | :delete | :f1 | :f2 | :f3 | :f4 | :f5 | :f6 | :f7 | :f8 | :f9 | :f10 | :f11 | :f12
```

What a key is. `:char` is a key that gives a character (see [`Key`](#term.Key)); the rest are the keys with those names.

### <a id="term.Key"></a>`Key`

```ec
rec Key
```

A key press.

**Fields**

- <a id="term.Key.name"></a>`name: KeyName` — Which key.
- <a id="term.Key.ch"></a>`ch: UInt = 0` — For `:char`, its Unicode code point, in lower case with Ctrl; otherwise 0.
- <a id="term.Key.ctrl"></a>`ctrl: Bool = false` — Whether Ctrl was held.
- <a id="term.Key.alt"></a>`alt: Bool = false` — Whether Alt was held.
- <a id="term.Key.shift"></a>`shift: Bool = false` — Whether Shift was held. Terminals only say for keys without a character.

### <a id="term.MouseAction"></a>`MouseAction`

```ec
declare type MouseAction as :press | :release | :drag | :scrollUp | :scrollDown
```

What happened with the mouse.

### <a id="term.MouseButton"></a>`MouseButton`

```ec
declare type MouseButton as :left | :middle | :right | :none
```

Which mouse button.

### <a id="term.Mouse"></a>`Mouse`

```ec
rec Mouse
```

A mouse event, after [`enableMouse`](#term.enableMouse).

**Fields**

- <a id="term.Mouse.action"></a>`action: MouseAction` — What happened.
- <a id="term.Mouse.button"></a>`button: MouseButton` — With which button.
- <a id="term.Mouse.col"></a>`col: Int` — The column.
- <a id="term.Mouse.row"></a>`row: Int` — The row.
- <a id="term.Mouse.ctrl"></a>`ctrl: Bool = false` — Whether Ctrl was held.
- <a id="term.Mouse.alt"></a>`alt: Bool = false` — Whether Alt was held.
- <a id="term.Mouse.shift"></a>`shift: Bool = false` — Whether Shift was held.

### <a id="term.Resize"></a>`Resize`

```ec
rec Resize
```

The terminal changed size, or the program continued after being stopped: draw again.

**Fields**

- <a id="term.Resize.size"></a>`size: Dimensions` — The new size.

### <a id="term.Eof"></a>`Eof`

```ec
rec Eof
```

The input ended.

**Fields**

- <a id="term.Eof.ended"></a>`ended: Bool = true` — Always true: an empty record can't be written.

### <a id="term.Event"></a>`Event`

```ec
type Event = Key | Mouse | Resize | Eof
```

What [`readEvent`](#term.readEvent) returns.

### <a id="term.Decoded"></a>`Decoded`

```ec
rec Decoded
```

What [`decode`](#term.decode) found at the start of the input.

**Fields**

- <a id="term.Decoded.event"></a>`event: Event?` — The event; null when these bytes mean nothing to act on.
- <a id="term.Decoded.used"></a>`used: Size` — The bytes used; 0 when more are needed to tell.

### <a id="term.Cell"></a>`Cell`

```ec
rec Cell
```

One cell of a [`Screen`](#term.Screen).

**Fields**

- <a id="term.Cell.ch"></a>`ch: UInt = 32` — The Unicode code point; 0 for the right half of a wide character.
- <a id="term.Cell.style"></a>`style: Style = Style { bold = false }` — How it looks.

### <a id="term.Screen"></a>`Screen`

```ec
mut ref rec Screen
```

A grid of cells to draw on, from [`screen`](#term.screen). Nothing shows until [`render`](#term.render), which writes only what
changed since the last time, so drawing everything again for each frame is fine.

**Fields**

- <a id="term.Screen.cols"></a>`cols: Int` — The width.
- <a id="term.Screen.rows"></a>`rows: Int` — The height.
- <a id="term.Screen.cells"></a>`cells: Cell[]` — What is being drawn.
- <a id="term.Screen.shown"></a>`shown: Cell[]` — What the terminal shows.
- <a id="term.Screen.stale"></a>`stale: Bool = true` — Whether the terminal's display must be drawn from nothing.
- <a id="term.Screen.cursorCol"></a>`cursorCol: Int = -1` — Where the cursor goes after drawing; -1 hides it.
- <a id="term.Screen.cursorRow"></a>`cursorRow: Int = 0` — See `cursorCol`.
- <a id="term.Screen.cursorShown"></a>`cursorShown: Bool = false` — Whether the last [`frame`](#term.frame) left the cursor visible.

### <a id="term.BoxKind"></a>`BoxKind`

```ec
declare type BoxKind as :single | :double | :rounded | :heavy
```

The lines of a [`box`](#term.box).

## Values

### <a id="term.DEFAULT"></a>`DEFAULT`

```ec
val DEFAULT = Color { 0 }
```

The terminal's own colour.

### <a id="term.BLACK"></a>`BLACK`

```ec
val BLACK = Color { 1 }
```

Palette colour 0.

### <a id="term.RED"></a>`RED`

```ec
val RED = Color { 2 }
```

Palette colour 1.

### <a id="term.GREEN"></a>`GREEN`

```ec
val GREEN = Color { 3 }
```

Palette colour 2.

### <a id="term.YELLOW"></a>`YELLOW`

```ec
val YELLOW = Color { 4 }
```

Palette colour 3.

### <a id="term.BLUE"></a>`BLUE`

```ec
val BLUE = Color { 5 }
```

Palette colour 4.

### <a id="term.MAGENTA"></a>`MAGENTA`

```ec
val MAGENTA = Color { 6 }
```

Palette colour 5.

### <a id="term.CYAN"></a>`CYAN`

```ec
val CYAN = Color { 7 }
```

Palette colour 6.

### <a id="term.WHITE"></a>`WHITE`

```ec
val WHITE = Color { 8 }
```

Palette colour 7.

### <a id="term.GRAY"></a>`GRAY`

```ec
val GRAY = Color { 9 }
```

Palette colour 8.

### <a id="term.BRIGHT_RED"></a>`BRIGHT_RED`

```ec
val BRIGHT_RED = Color { 10 }
```

Palette colour 9.

### <a id="term.BRIGHT_GREEN"></a>`BRIGHT_GREEN`

```ec
val BRIGHT_GREEN = Color { 11 }
```

Palette colour 10.

### <a id="term.BRIGHT_YELLOW"></a>`BRIGHT_YELLOW`

```ec
val BRIGHT_YELLOW = Color { 12 }
```

Palette colour 11.

### <a id="term.BRIGHT_BLUE"></a>`BRIGHT_BLUE`

```ec
val BRIGHT_BLUE = Color { 13 }
```

Palette colour 12.

### <a id="term.BRIGHT_MAGENTA"></a>`BRIGHT_MAGENTA`

```ec
val BRIGHT_MAGENTA = Color { 14 }
```

Palette colour 13.

### <a id="term.BRIGHT_CYAN"></a>`BRIGHT_CYAN`

```ec
val BRIGHT_CYAN = Color { 15 }
```

Palette colour 14.

### <a id="term.BRIGHT_WHITE"></a>`BRIGHT_WHITE`

```ec
val BRIGHT_WHITE = Color { 16 }
```

Palette colour 15.

### <a id="term.PLAIN"></a>`PLAIN`

```ec
val PLAIN = Style { bold = false }
```

The look of text with no style.

## Functions

### <a id="term.isTerminal"></a>`isTerminal`

```ec
fn isTerminal(): Bool
```

Whether stdout is a terminal, and not a file or a pipe.

### <a id="term.isInteractive"></a>`isInteractive`

```ec
fn isInteractive(): Bool
```

Whether stdin and stdout are both terminals, so a person can answer.

### <a id="term.size"></a>`size`

```ec
fn size(): Dimensions
```

The size of the terminal. Without one, the COLUMNS and LINES environment variables say, or else 80 by 24.

### <a id="term.palette"></a>`palette`

```ec
fn palette(index: UChar): Color
```

A colour of the 256-colour palette: 0 to 15 are the named colours, the rest a colour cube and grays.

### <a id="term.rgb"></a>`rgb`

```ec
fn rgb(red: UChar, green: UChar, blue: UChar): Color
```

A 24-bit colour. Most terminals show it; some show the nearest they have.

### <a id="term.setColorMode"></a>`setColorMode`

```ec
fn setColorMode(mode: ColorMode)
```

Sets when colours are used; see [`ColorMode`](#term.ColorMode).

### <a id="term.colorsEnabled"></a>`colorsEnabled`

```ec
fn colorsEnabled(): Bool
```

Whether [`styled`](#term.styled) adds colours now.

### <a id="term.styled"></a>`styled`

```ec
fn styled(s: String, style: Style): String
```

`s` in `style`, ready to print; `s` itself when colours are off (see [`ColorMode`](#term.ColorMode)).

### <a id="term.color"></a>`color`

```ec
fn color(s: String, c: Color): String
```

`s` in a colour: `io.log(term.color("failed", term.RED))`.

### <a id="term.bold"></a>`bold`

```ec
fn bold(s: String): String
```

`s` in bold.

### <a id="term.dim"></a>`dim`

```ec
fn dim(s: String): String
```

`s`, faint.

### <a id="term.underline"></a>`underline`

```ec
fn underline(s: String): String
```

`s`, underlined.

### <a id="term.charWidth"></a>`charWidth`

```ec
fn charWidth(cp: UInt): Int
```

How many columns a character takes: 2 for the wide ones (Chinese, Japanese, Korean, most emoji), 0 for combining
marks and control characters, 1 for the rest. Like C's wcwidth, without depending on the locale.

### <a id="term.stripStyles"></a>`stripStyles`

```ec
fn stripStyles(s: String): String
```

`s` without its escape sequences: what [`styled`](#term.styled) added is gone.

### <a id="term.displayWidth"></a>`displayWidth`

```ec
fn displayWidth(s: String): Size
```

How many columns `s` takes on the terminal, which is often not `s.len`. Escape sequences take none.

### <a id="term.fit"></a>`fit`

```ec
fn fit(s: String, width: Size): String
```

`s` made to take exactly `width` columns: spaces are added, or the end is cut off and replaced with "…".
For text without escape sequences.

### <a id="term.moveTo"></a>`moveTo`

```ec
fn moveTo(col: Int, row: Int)
```

Moves the cursor to a column and row.

### <a id="term.moveUp"></a>`moveUp`

```ec
fn moveUp(n: Int = 1)
```

Moves the cursor `n` rows up.

### <a id="term.moveDown"></a>`moveDown`

```ec
fn moveDown(n: Int = 1)
```

Moves the cursor `n` rows down.

### <a id="term.moveRight"></a>`moveRight`

```ec
fn moveRight(n: Int = 1)
```

Moves the cursor `n` columns right.

### <a id="term.moveLeft"></a>`moveLeft`

```ec
fn moveLeft(n: Int = 1)
```

Moves the cursor `n` columns left.

### <a id="term.clearDisplay"></a>`clearDisplay`

```ec
fn clearDisplay()
```

Clears the whole display and moves the cursor to the top left.

### <a id="term.clearLine"></a>`clearLine`

```ec
fn clearLine()
```

Clears the cursor's row and moves the cursor to its start.

### <a id="term.clearBelow"></a>`clearBelow`

```ec
fn clearBelow()
```

Clears from the cursor to the end of the display.

### <a id="term.hideCursor"></a>`hideCursor`

```ec
fn hideCursor()
```

Hides the cursor. It comes back when the program ends.

### <a id="term.showCursor"></a>`showCursor`

```ec
fn showCursor()
```

Shows the cursor.

### <a id="term.setTitle"></a>`setTitle`

```ec
fn setTitle(title: String)
```

Sets the title of the terminal's window.

### <a id="term.bell"></a>`bell`

```ec
fn bell()
```

Rings the terminal's bell.

### <a id="term.enterRaw"></a>`enterRaw`

```ec
fn enterRaw(keepSignals: Bool = true) throws IoError
```

Raw mode: keys arrive as they are pressed and aren't shown, instead of when Enter is pressed. Nothing happens if
it is on already. Leave it with [`leaveRaw`](#term.leaveRaw), in a `defer`.

**Parameters**

- `keepSignals` — whether Ctrl-C and Ctrl-Z still interrupt and stop the program; with false they are keys

**Throws**

- `IoError` — when stdin isn't a terminal

### <a id="term.leaveRaw"></a>`leaveRaw`

```ec
fn leaveRaw()
```

Puts back the settings [`enterRaw`](#term.enterRaw) changed.

### <a id="term.isRaw"></a>`isRaw`

```ec
fn isRaw(): Bool
```

Whether raw mode is on.

### <a id="term.enableMouse"></a>`enableMouse`

```ec
fn enableMouse()
```

Sends mouse events too: [`readEvent`](#term.readEvent) then returns [`Mouse`](#term.Mouse)s. Clicks, drags and the wheel are reported.

### <a id="term.disableMouse"></a>`disableMouse`

```ec
fn disableMouse()
```

Stops sending mouse events.

### <a id="term.enterFullscreen"></a>`enterFullscreen`

```ec
fn enterFullscreen(keepSignals: Bool = true) throws IoError
```

Full-screen mode: raw mode on the alternate screen, without the cursor. What was on the terminal comes back when
it ends. stderr is held back until then. Leave it with [`leaveFullscreen`](#term.leaveFullscreen), in a `defer`.

**Parameters**

- `keepSignals` — as in [`enterRaw`](#term.enterRaw)

**Throws**

- `IoError` — when stdin isn't a terminal

### <a id="term.leaveFullscreen"></a>`leaveFullscreen`

```ec
fn leaveFullscreen()
```

Ends full-screen mode, and writes out what stderr held back.

### <a id="term.isChar"></a>`isChar`

```ec
fn isChar(k: Key, c: Char): Bool
```

Whether `k` is the character `c` without Ctrl or Alt: `term.isChar(k, 'q')`.

### <a id="term.isCtrl"></a>`isCtrl`

```ec
fn isCtrl(k: Key, c: Char): Bool
```

Whether `k` is Ctrl and the letter `c`: `term.isCtrl(k, 'c')`.

### <a id="term.keyText"></a>`keyText`

```ec
fn keyText(k: Key): String
```

The text a key gives, as a String: "" when it has none.

### <a id="term.decode"></a>`decode`

```ec
fn decode(s: String, final: Bool = true): Decoded
```

Decodes the first key, mouse event or escape sequence of input bytes. [`readEvent`](#term.readEvent) uses this; it is public
for testing a program's input handling without a terminal.

**Parameters**

- `s` — the bytes
- `final` — whether no more bytes will come: then a lone ESC is the Esc key, instead of the start of a sequence

**Returns** the event and the bytes it took

### <a id="term.readEvent"></a>`readEvent`

```ec
fn readEvent(): Event
```

Waits for the next key, mouse event, change of size or the end of the input. Without raw mode, a terminal only
passes on input when Enter is pressed.

### <a id="term.pollEvent"></a>`pollEvent`

```ec
fn pollEvent(micros: Long): Event?
```

Like [`readEvent`](#term.readEvent), but gives up after a while, for a program that also has other things to do.

**Parameters**

- `micros` — how long to wait, in microseconds

**Returns** the event; null when the time is up

### <a id="term.readKey"></a>`readKey`

```ec
fn readKey(): Key?
```

Waits for the next key, skipping other events. @return the key; null at the end of the input

### <a id="term.screen"></a>`screen`

```ec
fn screen(cols: Int, rows: Int): Screen
```

A screen of a given size, in the current pool.

### `screen`

```ec
fn screen(): Screen
```

A screen the size of the terminal, in the current pool.

### <a id="term.resize"></a>`resize`

```ec
fn resize(s: Screen, d: Dimensions)
```

Changes the size of `s`, after a [`Resize`](#term.Resize) event. What it holds is cleared, and all of it is drawn again.

### <a id="term.invalidate"></a>`invalidate`

```ec
fn invalidate(s: Screen)
```

Makes [`render`](#term.render) draw everything again, after something else wrote to the terminal.

### <a id="term.clear"></a>`clear`

```ec
fn clear(s: Screen)
```

Fills `s` with spaces.

### <a id="term.put"></a>`put`

```ec
fn put(s: Screen, col: Int, row: Int, ch: UInt, style: Style = PLAIN)
```

Puts a character in a cell. Outside the screen, nothing happens; a wide character that doesn't fit is a space.

### <a id="term.text"></a>`text`

```ec
fn text(s: Screen, col: Int, row: Int, value: String, style: Style = PLAIN): Int
```

Writes text from a cell onwards, on one row: what doesn't fit is cut off. Line breaks and escape sequences aren't
interpreted, so don't pass what [`styled`](#term.styled) made; use `style`.

**Returns** the column after the text

### <a id="term.fill"></a>`fill`

```ec
fn fill(s: Screen, col: Int, row: Int, cols: Int, rows: Int, ch: UInt = 32, style: Style = PLAIN)
```

Fills a rectangle with a character.

### <a id="term.horizontalLine"></a>`horizontalLine`

```ec
fn horizontalLine(s: Screen, col: Int, row: Int, count: Int, style: Style = PLAIN)
```

A row of `count` horizontal line characters, as used in [`box`](#term.box).

### <a id="term.verticalLine"></a>`verticalLine`

```ec
fn verticalLine(s: Screen, col: Int, row: Int, count: Int, style: Style = PLAIN)
```

A column of `count` vertical line characters.

### <a id="term.box"></a>`box`

```ec
fn box(s: Screen, col: Int, row: Int, cols: Int, rows: Int, kind: BoxKind = :single, title: String = "", style: Style = PLAIN)
```

Draws a frame, inside the rectangle's edge.

**Parameters**

- `kind` — the lines to use
- `title` — text in the top edge; "" for none
- `style` — how the frame and title look

### <a id="term.placeCursor"></a>`placeCursor`

```ec
fn placeCursor(s: Screen, col: Int, row: Int)
```

Shows the cursor at a cell after [`render`](#term.render), as for a text field.

### <a id="term.removeCursor"></a>`removeCursor`

```ec
fn removeCursor(s: Screen)
```

Leaves the cursor hidden after [`render`](#term.render).

### <a id="term.frame"></a>`frame`

```ec
fn frame(s: Screen): String
```

The escape sequences that bring the terminal up to date with `s`, and marks them as done. [`render`](#term.render) writes
them; this is for testing what a program draws.

### <a id="term.render"></a>`render`

```ec
fn render(s: Screen)
```

Shows what was drawn on `s`, writing only the cells that changed.

### <a id="term.ask"></a>`ask`

```ec
fn ask(question: String): String?
```

Asks for a line of text: `val name = term.ask("Name: ") ?? ""`.

**Parameters**

- `question` — what to show before the answer; put a space at its end

**Returns** the answer; null when it was cancelled with Esc or Ctrl-D, or at the end of the input

### <a id="term.password"></a>`password`

```ec
fn password(question: String): String?
```

Asks for a secret, without showing it.

**Parameters**

- `question` — what to show before the answer

**Returns** the answer; null when it was cancelled with Esc or Ctrl-D, or at the end of the input

### <a id="term.confirm"></a>`confirm`

```ec
fn confirm(question: String, otherwise: Bool = false): Bool
```

Asks a yes or no question and waits for y or n.

**Parameters**

- `question` — what to ask; " [y/N] " is added
- `otherwise` — the answer for Enter, Esc and the end of the input

**Returns** the answer

### <a id="term.select"></a>`select`

```ec
fn select(title: String, options: String[], start: Size = 0): Size?
```

Lets the user pick from a list with the arrow keys, Home, End and Enter. A list taller than the terminal scrolls.

    if val i = term.select("Colour", names) do
        io.log(names[i])

**Parameters**

- `title` — what to show above the list
- `options` — what to pick from
- `start` — the option selected at first

**Returns** the index of the option picked; null for Esc, q or the end of the input, and when there are no options

### <a id="term.progressBar"></a>`progressBar`

```ec
fn progressBar(done: Size, total: Size, width: Size): String
```

A bar of `width` columns, filled as far as `done` of `total` gets: `████░░░░`.

### <a id="term.progress"></a>`progress`

```ec
fn progress(label: String, done: Size, total: Size)
```

Shows progress on the current row, replacing what the last call showed; at the end it moves to the next row.
With output that isn't a terminal, it only writes the last one.

    for i in 0..=files.len do
        term.progress("copying", i, files.len)

### <a id="term.spinner"></a>`spinner`

```ec
fn spinner(tick: Size): String
```

One frame of a spinner: show it with a growing `tick`, about ten times a second.
