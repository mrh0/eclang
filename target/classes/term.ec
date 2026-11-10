/**
 * Terminal tools: colours and styles, the cursor, key and mouse input, a screen buffer to draw on, and prompts.
 * Waiting, reading and writing use APR (apr_poll, apr_file_*, apr_signal); the terminal's own settings use termios,
 * which APR doesn't cover, so this file needs a POSIX system.
 *
 *     use "core:term.ec"
 *     io.log(term.color("done", term.GREEN))             // plain text when stdout isn't a terminal
 *     val ok = term.confirm("Delete it?")
 *     val pick = term.select("Colour", ["red", "green"]) // arrow keys and Enter; null for Esc
 *
 * A full-screen program draws on a {@link Screen} and reacts to events:
 *
 *     try term.enterFullscreen()
 *     defer term.leaveFullscreen()
 *     val s = term.screen()
 *     while true do
 *         term.clear(s)
 *         term.box(s, 0, 0, s.cols, s.rows, :rounded, "Demo")
 *         term.text(s, 2, 1, "press q", term.Style { bold = true })
 *         term.render(s)                                 // writes only the cells that changed
 *         val e = term.readEvent()
 *         when e do
 *             is term.Key do break if term.isChar(e, 'q')
 *             is term.Resize do term.resize(s, e.size)
 *             is term.Mouse do continue
 *             is term.Eof do break
 *
 * Columns and rows count from 0, at the top left. A loop that allocates should have a `pool do` block in its body.
 *
 * The terminal is put back when the program ends, also after a panic or a fatal signal. In full-screen mode stderr
 * is held back, so it doesn't scribble on the screen, and comes out when the mode ends: a panic's message stays
 * readable. Ctrl-Z works too: the terminal is put back while the program is stopped, and a {@link Resize} event
 * after it continues says to draw again.
 *
 * Input is read past C's stdio, so don't use `io.readLine` and these functions on the same piped input: stdio
 * reads ahead. Output can be mixed with `io.print`.
 */
module "term"
use "core:errors.ec"
use "core:string.ec"
use "core:unicode.ec"
use "core:c/stdio.ec"
use "core:c/stdlib.ec"
use "core:c/unistd.ec"
use "core:c/termios.ec"
use "core:c/ioctl.ec"
use "core:c/signal.ec"
use "core:apr/errno.ec"
use "core:apr/env.ec"
use "core:apr/file_io.ec"
use "core:apr/poll.ec"
use "core:apr/signal.ec"
use "core:apr/time.ec"

// ---------------------------------------------------------------------------------------------------
// The terminal

/** A size in character cells. */
rec Dimensions as
    /** Columns: the width. */
    cols: Int
    /** Rows: the height. */
    rows: Int

/** The APR file of stdin. Opened once, in the root pool, because it is used from any pool. */
priv fn openStdin(): @apr_file_t do
    var f: @apr_file_t? = null
    apr_file_open_stdin(mut f, pool)
    ret f!

/** The APR file of stdout. It has no buffer of its own. */
priv fn openStdout(): @apr_file_t do
    var f: @apr_file_t? = null
    apr_file_open_stdout(mut f, pool)
    ret f!

priv val input = openStdin()
priv val output = openStdout()

/** Whether stdout is a terminal, and not a file or a pipe. */
fn isTerminal(): Bool = isatty(STDOUT_FILENO) == 1

/** Whether stdin and stdout are both terminals, so a person can answer. */
fn isInteractive(): Bool = isatty(STDIN_FILENO) == 1 and isTerminal()

/** The value of the environment variable `name`, or null when it isn't set. */
priv fn envValue(name: CString): String? do
    var value: CString = ""c
    ret null if apr_env_get(mut value, name, pool) != APR_SUCCESS
    ret fromC(value)

/** The whole number in the environment variable `name`, or `otherwise`. */
priv fn envInt(name: CString, otherwise: Int): Int do
    if val text = envValue(name) do
        val n = parseInt(text) ?? 0
        ret if n > 0 and n < 100000 do n as Int else otherwise
    ret otherwise

/** The size of the terminal. Without one, the COLUMNS and LINES environment variables say, or else 80 by 24. */
fn size(): Dimensions do
    var w: winsize
    if ioctl(STDOUT_FILENO, TIOCGWINSZ, addrof(w)) == 0 and w.ws_col > 0 and w.ws_row > 0 do
        ret Dimensions { w.ws_col as Int, w.ws_row as Int }
    ret Dimensions { envInt("COLUMNS"c, 80), envInt("LINES"c, 24) }

/** Writes `s` after what `io.print` holds back, so the two stay in order. */
priv fn emit(s: String) do
    fflush(stdout)
    var written: apr_size_t = 0
    apr_file_write_full(output, s.data, s.len, mut written)

// ---------------------------------------------------------------------------------------------------
// Colours and styles

/** A colour. Use the constants, {@link rgb} or {@link palette}. */
rec Color as
    /** 0: the terminal's default; 1 to 256: a palette index plus 1; from RGB_FLAG: red, green and blue, 8 bits each. */
    code: Int = 0

priv const RGB_FLAG: Int = 0x1000000

/** The terminal's own colour. */
val DEFAULT = Color { 0 }
/** Palette colour 0. */
val BLACK = Color { 1 }
/** Palette colour 1. */
val RED = Color { 2 }
/** Palette colour 2. */
val GREEN = Color { 3 }
/** Palette colour 3. */
val YELLOW = Color { 4 }
/** Palette colour 4. */
val BLUE = Color { 5 }
/** Palette colour 5. */
val MAGENTA = Color { 6 }
/** Palette colour 6. */
val CYAN = Color { 7 }
/** Palette colour 7. */
val WHITE = Color { 8 }
/** Palette colour 8. */
val GRAY = Color { 9 }
/** Palette colour 9. */
val BRIGHT_RED = Color { 10 }
/** Palette colour 10. */
val BRIGHT_GREEN = Color { 11 }
/** Palette colour 11. */
val BRIGHT_YELLOW = Color { 12 }
/** Palette colour 12. */
val BRIGHT_BLUE = Color { 13 }
/** Palette colour 13. */
val BRIGHT_MAGENTA = Color { 14 }
/** Palette colour 14. */
val BRIGHT_CYAN = Color { 15 }
/** Palette colour 15. */
val BRIGHT_WHITE = Color { 16 }

/** A colour of the 256-colour palette: 0 to 15 are the named colours, the rest a colour cube and grays. */
fn palette(index: UChar): Color = Color { (index as Int) + 1 }

/** A 24-bit colour. Most terminals show it; some show the nearest they have. */
fn rgb(red: UChar, green: UChar, blue: UChar): Color do
    ret Color { RGB_FLAG | ((red as Int) << 16) | ((green as Int) << 8) | (blue as Int) }

/** How text looks. Name what you want: `Style { fg = term.RED, bold = true }`. */
rec Style as
    /** The colour of the text. */
    fg: Color = Color { 0 }
    /** The colour behind the text. */
    bg: Color = Color { 0 }
    /** Thick text. */
    bold: Bool = false
    /** Faint text. */
    dim: Bool = false
    /** Slanted text. */
    italic: Bool = false
    /** Underlined text. */
    underline: Bool = false
    /** Text and background colours swapped. */
    reverse: Bool = false
    /** Crossed-out text. */
    strike: Bool = false

/** The look of text with no style. */
val PLAIN = Style { bold = false }

/**
 * When {@link styled} and the functions built on it add colours:
 *
 * - `:auto`: when stdout is a terminal and the NO_COLOR environment variable and TERM=dumb don't say otherwise
 * - `:always`: always, for output that goes to a pager, say
 * - `:never`: never
 */
declare type ColorMode as :auto | :always | :never

priv var colorMode: ColorMode = :auto

/** Sets when colours are used; see {@link ColorMode}. */
fn setColorMode(mode: ColorMode) do
    colorMode = mode

/** Whether {@link styled} adds colours now. */
fn colorsEnabled(): Bool do
    ret true if colorMode == :always
    ret false if colorMode == :never or not isTerminal()
    ret false if envValue("NO_COLOR"c) != null
    if val name = envValue("TERM"c) do
        ret name != "dumb"
    ret true

/** Adds the codes of a colour: `base` is 30 for text, 40 for background. */
priv fn appendColor(b: StringBuilder, c: Color, base: Int) do
    if c.code >= RGB_FLAG do
        b.append(";")
        b.append((base + 8) as Long)
        b.append(";2;")
        b.append(((c.code >> 16) & 255) as Long)
        b.append(";")
        b.append(((c.code >> 8) & 255) as Long)
        b.append(";")
        b.append((c.code & 255) as Long)
    else if c.code > 16 do
        b.append(";")
        b.append((base + 8) as Long)
        b.append(";5;")
        b.append((c.code - 1) as Long)
    else if c.code > 8 do
        b.append(";")
        b.append((base + 60 + c.code - 9) as Long)
    else if c.code > 0 do
        b.append(";")
        b.append((base + c.code - 1) as Long)

/** Adds the escape sequence that selects `style`, whatever was selected before. */
priv fn appendStyle(b: StringBuilder, style: Style) do
    b.append("\x1b[0")
    b.append(";1") if style.bold
    b.append(";2") if style.dim
    b.append(";3") if style.italic
    b.append(";4") if style.underline
    b.append(";7") if style.reverse
    b.append(";9") if style.strike
    appendColor(b, style.fg, 30)
    appendColor(b, style.bg, 40)
    b.append("m")

/** `s` in `style`, ready to print; `s` itself when colours are off (see {@link ColorMode}). */
fn styled(s: String, style: Style): String do
    ret s if not colorsEnabled() or style == PLAIN
    val b = stringBuilder(s.len + 24)
    appendStyle(b, style)
    b.append(s)
    b.append("\x1b[0m")
    ret b.build()

/** `s` in a colour: `io.log(term.color("failed", term.RED))`. */
fn color(s: String, c: Color): String = styled(s, Style { fg = c })

/** `s` in bold. */
fn bold(s: String): String = styled(s, Style { bold = true })

/** `s`, faint. */
fn dim(s: String): String = styled(s, Style { dim = true })

/** `s`, underlined. */
fn underline(s: String): String = styled(s, Style { underline = true })

// ---------------------------------------------------------------------------------------------------
// Text width

/**
 * How many columns a character takes: 2 for the wide ones (Chinese, Japanese, Korean, most emoji), 0 for combining
 * marks and control characters, 1 for the rest. Like C's wcwidth, without depending on the locale.
 */
fn charWidth(cp: UInt): Int = unicode.width(cp)

/** The byte every escape sequence starts with. */
priv const ESC: Char = 27

/** The index after the escape sequence that starts at byte `i` of `s`. */
priv fn skipEscape(s: String, i: Size): Size do
    var at = i + 1
    ret at if at >= s.len or s[at] != '['
    at += 1
    while at < s.len and (s[at] < '@' or s[at] > '~') do
        at += 1
    ret if at < s.len do at + 1 else at

/** `s` without its escape sequences: what {@link styled} added is gone. */
fn stripStyles(s: String): String do
    val b = stringBuilder(s.len + 1)
    var i: Size = 0
    while i < s.len do
        if s[i] == ESC do
            i = skipEscape(s, i)
        else do
            b.append(s[i])
            i += 1
    ret b.build()

/** How many columns `s` takes on the terminal, which is often not `s.len`. Escape sequences take none. */
fn displayWidth(s: String): Size do
    var width: Size = 0
    var i: Size = 0
    while i < s.len do
        if s[i] == ESC do
            i = skipEscape(s, i)
        else do
            val (cp, n) = unicode.decode(s, i)
            width += charWidth(cp) as Size
            i += n
    ret width

/**
 * `s` made to take exactly `width` columns: spaces are added, or the end is cut off and replaced with "…".
 * For text without escape sequences.
 */
fn fit(s: String, width: Size): String do
    val have = displayWidth(s)
    ret concat(s, repeat(" ", width - have)) if have <= width
    ret "" if width == 0
    val b = stringBuilder(s.len + 4)
    var used: Size = 0
    var i: Size = 0
    while i < s.len do
        val (cp, n) = unicode.decode(s, i)
        val w = charWidth(cp) as Size
        break if used + w > width - 1
        b.append(s[i..<i + n])
        used += w
        i += n
    b.append("…")
    b.append(repeat(" ", width - 1 - used))
    ret b.build()

// ---------------------------------------------------------------------------------------------------
// The cursor and the display

/** Moves the cursor to a column and row. */
fn moveTo(col: Int, row: Int) do
    emit("\x1b[${row + 1};${col + 1}H")

/** Moves the cursor `n` rows up. */
fn moveUp(n: Int = 1) do
    emit("\x1b[${n}A") if n > 0

/** Moves the cursor `n` rows down. */
fn moveDown(n: Int = 1) do
    emit("\x1b[${n}B") if n > 0

/** Moves the cursor `n` columns right. */
fn moveRight(n: Int = 1) do
    emit("\x1b[${n}C") if n > 0

/** Moves the cursor `n` columns left. */
fn moveLeft(n: Int = 1) do
    emit("\x1b[${n}D") if n > 0

/** Clears the whole display and moves the cursor to the top left. */
fn clearDisplay() do
    emit("\x1b[2J\x1b[H")

/** Clears the cursor's row and moves the cursor to its start. */
fn clearLine() do
    emit("\r\x1b[2K")

/** Clears from the cursor to the end of the display. */
fn clearBelow() do
    emit("\x1b[J")

priv var cursorHidden = false

/** Hides the cursor. It comes back when the program ends. */
fn hideCursor() do
    installHandlers()
    cursorHidden = true
    emit("\x1b[?25l")

/** Shows the cursor. */
fn showCursor() do
    cursorHidden = false
    emit("\x1b[?25h")
    releaseHandlers()

/** Sets the title of the terminal's window. */
fn setTitle(title: String) do
    emit("\x1b]0;${title}\x07")

/** Rings the terminal's bell. */
fn bell() do
    emit("\x07")

// ---------------------------------------------------------------------------------------------------
// Terminal modes, and putting them back

/** Empty settings; a global needs a value. */
priv fn noSettings(): termios do
    var settings: termios
    ret settings

/** The settings from before raw mode. */
priv var original = noSettings()
/** Whether `original` holds settings to put back. */
priv var rawActive = false
priv var fullscreenActive = false
priv var mouseActive = false
priv var handlersActive = false
priv var exitHookSet = false
/** Set by the handler of SIGWINCH and after a stop; the next event is a Resize. */
priv var resized = false
/** Where stderr collects in full-screen mode, so a panic's message survives leaving the alternate screen. */
priv var heldErrors: CString? = null

priv val HANDLED = [SIGINT, SIGTERM, SIGHUP, SIGQUIT, SIGABRT, SIGSEGV, SIGBUS, SIGFPE, SIGILL]

/** Writes straight to the terminal. Unlike {@link emit}, this is safe in a signal handler. */
priv fn writeDirect(s: String) do
    write(STDOUT_FILENO, s.data, s.len)

/** Undoes what the modes changed, in a way that is safe in a signal handler. The mode flags stay as they are. */
priv fn undoModes() do
    writeDirect("\x1b[?1006l\x1b[?1002l") if mouseActive
    writeDirect("\x1b[0m") if fullscreenActive
    writeDirect("\x1b[?1049l") if fullscreenActive
    writeDirect("\x1b[?25h") if cursorHidden or fullscreenActive
    tcsetattr(STDIN_FILENO, TCSADRAIN, addrof(original)) if rawActive

/** Sets the modes again after a stop. */
priv fn redoModes() do
    var settings = original
    if rawActive do
        applyRaw(mut settings, rawKeepsSignals)
        tcsetattr(STDIN_FILENO, TCSADRAIN, addrof(settings))
    writeDirect("\x1b[?1049h") if fullscreenActive
    writeDirect("\x1b[?25l") if cursorHidden or fullscreenActive
    writeDirect("\x1b[?1002h\x1b[?1006h") if mouseActive

/** Puts the terminal back, then lets the signal do what it does by default. */
priv fn onFatalSignal(signo: Int) do
    undoModes()
    // The panic's message; only then, because stdio isn't safe in the middle of anything else.
    fflush(stderr) if signo == SIGABRT and heldErrors != null
    apr_signal(signo, SIG_DFL)
    raise(signo)

/** Ctrl-Z: leaves the terminal usable while stopped, and asks for a full redraw afterwards. */
priv fn onStop(signo: Int) do
    undoModes()
    raise(SIGSTOP)
    redoModes()
    resized = true

/** The terminal's size changed. */
priv fn onResize(signo: Int) do
    resized = true

/** Puts everything back when the program ends with `exit` or by returning from `main`. */
priv fn atExit() do
    undoModes()
    if heldErrors != null do
        fflush(stderr)
        setvbuf(stderr, null, _IONBF, 0)
    rawActive = false
    fullscreenActive = false
    mouseActive = false

/** Sets the handlers that put the terminal back. */
priv fn installHandlers() do
    if not exitHookSet do
        exitHookSet = true
        atexit(fn atExit)
    if not handlersActive do
        handlersActive = true
        for signo in HANDLED do
            apr_signal(signo, fn onFatalSignal)
        apr_signal(SIGTSTP, fn onStop)
        apr_signal(SIGWINCH, fn onResize)

/** Takes the handlers away again, once nothing is left to put back. */
priv fn releaseHandlers() do
    if handlersActive and not rawActive and not fullscreenActive and not cursorHidden and not mouseActive do
        handlersActive = false
        for signo in HANDLED do
            apr_signal(signo, SIG_DFL)
        apr_signal(SIGTSTP, SIG_DFL)
        apr_signal(SIGWINCH, SIG_DFL)

priv var rawKeepsSignals = true

/** Changes `settings` to raw mode, keeping "\n" as a line break for what `io.log` prints. */
priv fn applyRaw(mut settings: termios, keepSignals: Bool) do
    cfmakeraw(mut settings)
    settings.c_oflag = settings.c_oflag | OPOST
    if keepSignals do
        settings.c_lflag = settings.c_lflag | ISIG

/**
 * Raw mode: keys arrive as they are pressed and aren't shown, instead of when Enter is pressed. Nothing happens if
 * it is on already. Leave it with {@link leaveRaw}, in a `defer`.
 *
 * @param keepSignals whether Ctrl-C and Ctrl-Z still interrupt and stop the program; with false they are keys
 * @throws when stdin isn't a terminal
 */
fn enterRaw(keepSignals: Bool = true) throws IoError do
    if not rawActive do
        throw ioError(apr_get_os_error(), "stdin") if tcgetattr(STDIN_FILENO, mut original) != 0
        var settings = original
        applyRaw(mut settings, keepSignals)
        throw ioError(apr_get_os_error(), "stdin") if tcsetattr(STDIN_FILENO, TCSADRAIN, addrof(settings)) != 0
        rawKeepsSignals = keepSignals
        rawActive = true
        installHandlers()

/** Puts back the settings {@link enterRaw} changed. */
fn leaveRaw() do
    if rawActive do
        fflush(stdout)
        tcsetattr(STDIN_FILENO, TCSADRAIN, addrof(original))
        rawActive = false
        releaseHandlers()

/** Whether raw mode is on. */
fn isRaw(): Bool = rawActive

/** Sends mouse events too: {@link readEvent} then returns {@link Mouse}s. Clicks, drags and the wheel are reported. */
fn enableMouse() do
    installHandlers()
    mouseActive = true
    emit("\x1b[?1002h\x1b[?1006h")

/** Stops sending mouse events. */
fn disableMouse() do
    mouseActive = false
    emit("\x1b[?1006l\x1b[?1002l")
    releaseHandlers()

/**
 * Full-screen mode: raw mode on the alternate screen, without the cursor. What was on the terminal comes back when
 * it ends. stderr is held back until then. Leave it with {@link leaveFullscreen}, in a `defer`.
 *
 * @param keepSignals as in {@link enterRaw}
 * @throws when stdin isn't a terminal
 */
fn enterFullscreen(keepSignals: Bool = true) throws IoError do
    if not fullscreenActive do
        try enterRaw(keepSignals)
        fullscreenActive = true
        if heldErrors == null do
            heldErrors = malloc(65536) !as CString?
        fflush(stderr)
        setvbuf(stderr, heldErrors, _IOFBF, 65536)
        emit("\x1b[?1049h\x1b[?25l\x1b[2J")

/** Ends full-screen mode, and writes out what stderr held back. */
fn leaveFullscreen() do
    if fullscreenActive do
        disableMouse() if mouseActive
        emit("\x1b[0m\x1b[?1049l\x1b[?25h")
        cursorHidden = false
        fullscreenActive = false
        leaveRaw()
        fflush(stderr)
        setvbuf(stderr, null, _IONBF, 0)

// ---------------------------------------------------------------------------------------------------
// Events

/**
 * What a key is. `:char` is a key that gives a character (see {@link Key}); the rest are the keys with those names.
 */
declare type KeyName as :char | :enter | :tab | :backspace | :escape | :up | :down | :left | :right | :home | :end | :pageUp | :pageDown | :insert | :delete | :f1 | :f2 | :f3 | :f4 | :f5 | :f6 | :f7 | :f8 | :f9 | :f10 | :f11 | :f12

/** A key press. */
rec Key as
    /** Which key. */
    name: KeyName
    /** For `:char`, its Unicode code point, in lower case with Ctrl; otherwise 0. */
    ch: UInt = 0
    /** Whether Ctrl was held. */
    ctrl: Bool = false
    /** Whether Alt was held. */
    alt: Bool = false
    /** Whether Shift was held. Terminals only say for keys without a character. */
    shift: Bool = false

/** What happened with the mouse. */
declare type MouseAction as :press | :release | :drag | :scrollUp | :scrollDown

/** Which mouse button. */
declare type MouseButton as :left | :middle | :right | :none

/** A mouse event, after {@link enableMouse}. */
rec Mouse as
    /** What happened. */
    action: MouseAction
    /** With which button. */
    button: MouseButton
    /** The column. */
    col: Int
    /** The row. */
    row: Int
    /** Whether Ctrl was held. */
    ctrl: Bool = false
    /** Whether Alt was held. */
    alt: Bool = false
    /** Whether Shift was held. */
    shift: Bool = false

/** The terminal changed size, or the program continued after being stopped: draw again. */
rec Resize as
    /** The new size. */
    size: Dimensions

/** The input ended. */
rec Eof as
    /** Always true: an empty record can't be written. */
    ended: Bool = true

/** What {@link readEvent} returns. */
type Event = Key | Mouse | Resize | Eof

/** Whether `k` is the character `c` without Ctrl or Alt: `term.isChar(k, 'q')`. */
fn isChar(k: Key, c: Char): Bool = k.name == :char and k.ch == (c as UChar) as UInt and not k.ctrl and not k.alt

/** Whether `k` is Ctrl and the letter `c`: `term.isCtrl(k, 'c')`. */
fn isCtrl(k: Key, c: Char): Bool = k.name == :char and k.ch == (c as UChar) as UInt and k.ctrl

/** The text a key gives, as a String: "" when it has none. */
fn keyText(k: Key): String do
    ret "" if k.name != :char
    val b = stringBuilder(4)
    unicode.appendCodePoint(b, k.ch)
    ret b.build()

/** What {@link decode} found at the start of the input. */
rec Decoded as
    /** The event; null when these bytes mean nothing to act on. */
    event: Event?
    /** The bytes used; 0 when more are needed to tell. */
    used: Size

/** A plain key without modifiers, `used` bytes long. */
priv fn plainKey(name: KeyName, used: Size): Decoded = Decoded { Key { name = name }, used }

/** The key of a single control byte, or a character. */
priv fn decodeByte(s: String, final: Bool): Decoded do
    val c = (s[0] as UChar) as UInt
    ret plainKey(:enter, 1) if c == 13 or c == 10
    ret plainKey(:tab, 1) if c == 9
    ret plainKey(:backspace, 1) if c == 127 or c == 8
    ret Decoded { Key { name = :char, ch = ' ' as UInt, ctrl = true }, 1 } if c == 0
    ret Decoded { Key { name = :char, ch = c + 96, ctrl = true }, 1 } if c < 27
    ret Decoded { Key { name = :char, ch = c + 64, ctrl = true }, 1 } if c < 32
    ret Decoded { Key { name = :char, ch = c }, 1 } if c < 128
    // UTF-8: more bytes may be on their way.
    val need: Size = if c >= 0xF0 do 4 else if c >= 0xE0 do 3 else 2
    ret Decoded { null, 0 } if c >= 0xC2 and s.len < need and not final
    val (cp, n) = unicode.decode(s, 0)
    ret Decoded { null, n } if cp == 0xFFFD and n == 1
    ret Decoded { Key { name = :char, ch = cp }, n }

/** `d`'s key with the modifiers of an xterm `mods` parameter (1 + shift + 2 alt + 4 ctrl). */
priv fn withModifiers(name: KeyName, mods: Int, used: Size): Decoded do
    val m = if mods > 0 do mods - 1 else 0
    ret Decoded { Key { name = name, shift = m & 1 != 0, alt = m & 2 != 0, ctrl = m & 4 != 0 }, used }

/** The key of the number in `ESC [ n ~`, or null. */
priv fn tildeKey(n: Int): KeyName? do
    when n do
        1, 7 do ret :home
        2 do ret :insert
        3 do ret :delete
        4, 8 do ret :end
        5 do ret :pageUp
        6 do ret :pageDown
        11 do ret :f1
        12 do ret :f2
        13 do ret :f3
        14 do ret :f4
        15 do ret :f5
        17 do ret :f6
        18 do ret :f7
        19 do ret :f8
        20 do ret :f9
        21 do ret :f10
        23 do ret :f11
        24 do ret :f12
        else do ret null

/** The key of the letter that ends `ESC [ ...` or `ESC O ...`, or null. */
priv fn letterKey(c: Char): KeyName? do
    when c do
        'A' do ret :up
        'B' do ret :down
        'C' do ret :right
        'D' do ret :left
        'H' do ret :home
        'F' do ret :end
        'P' do ret :f1
        'Q' do ret :f2
        'R' do ret :f3
        'S' do ret :f4
        else do ret null

/** The event of `ESC [ < button ; col ; row M` (pressed) or `m` (released), with the numbers read already. */
priv fn mouseEvent(code: Int, col: Int, row: Int, released: Bool, used: Size): Decoded do
    val low = code & 3
    val button: MouseButton = if low == 0 do :left else if low == 1 do :middle else if low == 2 do :right else :none
    val shift = code & 4 != 0
    val alt = code & 8 != 0
    val ctrl = code & 16 != 0
    var action: MouseAction = :press
    if code & 64 != 0 do
        action = if low == 0 do :scrollUp else :scrollDown
    else if released do
        action = :release
    else if code & 32 != 0 do
        action = :drag
    val b: MouseButton = if code & 64 != 0 do :none else button
    ret Decoded { Mouse { action = action, button = b, col = col - 1, row = row - 1, ctrl = ctrl, alt = alt, shift = shift }, used }

/** Decodes `ESC [ ...`, `s` starting at the ESC. */
priv fn decodeCsi(s: String, final: Bool): Decoded do
    var i: Size = 2
    val isMouse = i < s.len and s[i] == '<'
    if isMouse do
        i += 1
    var first = 0
    var second = 0
    var third = 0
    var index = 0
    while i < s.len and (s[i] < '@' or s[i] > '~') do
        val c = s[i]
        if c == ';' do
            index += 1
        else if isDigit(c) do
            val digit = (c - '0') as Int
            if index == 0 do
                first = first * 10 + digit
            else if index == 1 do
                second = second * 10 + digit
            else if index == 2 do
                third = third * 10 + digit
        i += 1
    if i >= s.len do
        // Cut off: wait for the rest, or drop it if there won't be any.
        ret Decoded { null, if final do s.len else 0 }
    val used = i + 1
    val last = s[i]
    ret mouseEvent(first, second, third, last == 'm', used) if isMouse
    if last == '~' do
        if val name = tildeKey(first) do
            ret withModifiers(name, second, used)
        ret Decoded { null, used }
    ret Decoded { Key { name = :tab, shift = true }, used } if last == 'Z'
    if val name = letterKey(last) do
        ret withModifiers(name, second, used)
    ret Decoded { null, used }

/**
 * Decodes the first key, mouse event or escape sequence of input bytes. {@link readEvent} uses this; it is public
 * for testing a program's input handling without a terminal.
 *
 * @param s the bytes
 * @param final whether no more bytes will come: then a lone ESC is the Esc key, instead of the start of a sequence
 * @return the event and the bytes it took
 */
fn decode(s: String, final: Bool = true): Decoded do
    ret Decoded { null, 0 } if s.len == 0
    ret decodeByte(s, final) if s[0] != ESC
    if s.len == 1 do
        ret if final do plainKey(:escape, 1) else Decoded { null, 0 }
    ret decodeCsi(s, final) if s[1] == '['
    if s[1] == 'O' do
        if s.len == 2 do
            ret if final do Decoded { Key { name = :char, ch = 'o' as UInt, alt = true }, 2 } else Decoded { null, 0 }
        if val name = letterKey(s[2]) do
            ret plainKey(name, 3)
        ret Decoded { null, 3 }
    ret plainKey(:escape, 1) if s[1] == ESC
    // Alt and a key: ESC, then the key.
    val inner = decodeByte(s[1..<s.len], final)
    ret Decoded { null, 0 } if inner.used == 0
    if val e = inner.event do
        when e do
            is Key do ret Decoded { { ...e, alt = true }, inner.used + 1 }
            else do ret Decoded { null, inner.used + 1 }
    ret Decoded { null, inner.used + 1 }

priv var pending = new Char[1024]
priv var pendingLen: Size = 0
priv var inputClosed = false

/** How long to wait for the rest of an escape sequence, in microseconds; after that, a lone ESC is the Esc key. */
priv const ESCAPE_WAIT: Long = 30000

/** Waits for input. @return the APR status: APR_SUCCESS, APR_TIMEUP or an interruption */
priv fn waitForInput(micros: Long): Int do
    var fd: apr_pollfd_t
    fd.desc_type = APR_POLL_FILE
    fd.reqevents = APR_POLLIN as Short
    fd.desc.f = input
    var ready: apr_int32_t = 0
    ret apr_poll(addrof(fd), 1, mut ready, micros)

/** Reads what stdin has into `pending`. */
priv fn fillPending() do
    var got: apr_size_t = pending.len - pendingLen
    val status = apr_file_read(input, addrof(pending[pendingLen]), mut got)
    if APR_STATUS_IS_EINTR(status) == 0 do
        if status != APR_SUCCESS do
            inputClosed = true
        else do
            pendingLen += got

/** Takes the first `n` bytes off `pending`. */
priv fn consume(n: Size) do
    for i in n..<pendingLen do
        pending[i - n] = pending[i]
    pendingLen -= n

/** The next event; null when `timeout` microseconds pass first. A negative `timeout` waits for as long as it takes. */
priv fn nextEvent(timeout: Long): Event? do
    fflush(stdout)
    val start = apr_time_now()
    var flushPartial = false
    while true do
        if resized do
            resized = false
            ret Resize { size() }
        val d = decode(pending[0..<pendingLen] !as String, inputClosed or flushPartial)
        flushPartial = false
        if d.used > 0 do
            consume(d.used)
            if val e = d.event do
                ret e
            continue
        ret Eof { ended = true } if inputClosed
        var wait = timeout
        if timeout >= 0 do
            wait = timeout - (apr_time_now() - start)
            if wait < 0 do
                wait = 0
        if pendingLen > 0 and (wait < 0 or wait > ESCAPE_WAIT) do
            wait = ESCAPE_WAIT
        val status = waitForInput(wait)
        if status == APR_SUCCESS do
            fillPending()
        else if APR_STATUS_IS_TIMEUP(status) != 0 do
            if pendingLen > 0 do
                flushPartial = true
            else do
                ret null
        else if APR_STATUS_IS_EINTR(status) == 0 do
            inputClosed = true

/**
 * Waits for the next key, mouse event, change of size or the end of the input. Without raw mode, a terminal only
 * passes on input when Enter is pressed.
 */
fn readEvent(): Event do
    while true do
        if val e = nextEvent(-1) do
            ret e

/**
 * Like {@link readEvent}, but gives up after a while, for a program that also has other things to do.
 * @param micros how long to wait, in microseconds
 * @return the event; null when the time is up
 */
fn pollEvent(micros: Long): Event? = nextEvent(micros)

/** Waits for the next key, skipping other events. @return the key; null at the end of the input */
fn readKey(): Key? do
    while true do
        val e = readEvent()
        when e do
            is Key do ret e
            is Eof do ret null
            else do continue

// ---------------------------------------------------------------------------------------------------
// The screen buffer

/** One cell of a {@link Screen}. */
rec Cell as
    /** The Unicode code point; 0 for the right half of a wide character. */
    ch: UInt = 32
    /** How it looks. */
    style: Style = Style { bold = false }

/**
 * A grid of cells to draw on, from {@link screen}. Nothing shows until {@link render}, which writes only what
 * changed since the last time, so drawing everything again for each frame is fine.
 */
mut ref rec Screen as
    /** The width. */
    cols: Int
    /** The height. */
    rows: Int
    /** What is being drawn. */
    cells: Cell[]
    /** What the terminal shows. */
    shown: Cell[]
    /** Whether the terminal's display must be drawn from nothing. */
    stale: Bool = true
    /** Where the cursor goes after drawing; -1 hides it. */
    cursorCol: Int = -1
    /** See `cursorCol`. */
    cursorRow: Int = 0
    /** Whether the last {@link frame} left the cursor visible. */
    cursorShown: Bool = false

/** `count` empty cells. */
priv fn blankCells(count: Size): Cell[] do
    val cells = new Cell[count]
    for i in 0..<count do
        cells[i] = Cell { 32, PLAIN }
    ret cells

/** A screen of a given size, in the current pool. */
fn screen(cols: Int, rows: Int): Screen do
    val count = (cols * rows) as Size
    ret new Screen { cols, rows, blankCells(count), blankCells(count) }

/** A screen the size of the terminal, in the current pool. */
fn screen(): Screen do
    val d = size()
    ret screen(d.cols, d.rows)

/** Changes the size of `s`, after a {@link Resize} event. What it holds is cleared, and all of it is drawn again. */
fn resize(s: Screen, d: Dimensions) do
    if d.cols != s.cols or d.rows != s.rows do
        val count = (d.cols * d.rows) as Size
        s.cols = d.cols
        s.rows = d.rows
        s.cells = blankCells(count)
        s.shown = blankCells(count)
    s.stale = true

/** Makes {@link render} draw everything again, after something else wrote to the terminal. */
fn invalidate(s: Screen) do
    s.stale = true

/** Fills `s` with spaces. */
fn clear(s: Screen) do
    for i in 0..<s.cells.len do
        s.cells[i] = Cell { 32, PLAIN }

/** The index of a cell. */
priv fn indexOf(s: Screen, col: Int, row: Int): Size = (row * s.cols + col) as Size

/** Whether a cell is on the screen. */
priv fn inside(s: Screen, col: Int, row: Int): Bool = col >= 0 and row >= 0 and col < s.cols and row < s.rows

/** Blanks the other half of the wide character at a cell, if it holds one, because that cell is drawn over. */
priv fn breakWide(s: Screen, col: Int, row: Int) do
    val i = indexOf(s, col, row)
    if s.cells[i].ch == 0 and col > 0 do
        s.cells[i - 1] = Cell { 32, s.cells[i - 1].style }
    if col + 1 < s.cols and s.cells[i + 1].ch == 0 do
        s.cells[i + 1] = Cell { 32, s.cells[i + 1].style }

/** Puts a character in a cell. Outside the screen, nothing happens; a wide character that doesn't fit is a space. */
fn put(s: Screen, col: Int, row: Int, ch: UInt, style: Style = PLAIN) do
    val width = charWidth(ch)
    if inside(s, col, row) and width > 0 do
        breakWide(s, col, row)
        if width == 2 and col + 1 >= s.cols do
            s.cells[indexOf(s, col, row)] = Cell { 32, style }
        else do
            s.cells[indexOf(s, col, row)] = Cell { ch, style }
            if width == 2 do
                breakWide(s, col + 1, row)
                s.cells[indexOf(s, col + 1, row)] = Cell { 0, style }

/**
 * Writes text from a cell onwards, on one row: what doesn't fit is cut off. Line breaks and escape sequences aren't
 * interpreted, so don't pass what {@link styled} made; use `style`.
 * @return the column after the text
 */
fn text(s: Screen, col: Int, row: Int, value: String, style: Style = PLAIN): Int do
    var at = col
    var i: Size = 0
    while i < value.len and at < s.cols do
        val (cp, n) = unicode.decode(value, i)
        put(s, at, row, cp, style)
        at += charWidth(cp)
        i += n
    ret at

/** Fills a rectangle with a character. */
fn fill(s: Screen, col: Int, row: Int, cols: Int, rows: Int, ch: UInt = 32, style: Style = PLAIN) do
    for y in row..<row + rows do
        for x in col..<col + cols do
            put(s, x, y, ch, style)

/** A row of `count` horizontal line characters, as used in {@link box}. */
fn horizontalLine(s: Screen, col: Int, row: Int, count: Int, style: Style = PLAIN) do
    fill(s, col, row, count, 1, 0x2500, style)

/** A column of `count` vertical line characters. */
fn verticalLine(s: Screen, col: Int, row: Int, count: Int, style: Style = PLAIN) do
    fill(s, col, row, 1, count, 0x2502, style)

/** The lines of a {@link box}. */
declare type BoxKind as :single | :double | :rounded | :heavy

/** The characters of a box, in the order horizontal, vertical, top left, top right, bottom left, bottom right. */
priv fn boxChars(kind: BoxKind): String do
    when kind do
        :single do ret "─│┌┐└┘"
        :double do ret "═║╔╗╚╝"
        :rounded do ret "─│╭╮╰╯"
        :heavy do ret "━┃┏┓┗┛"

/** The `n`th character of {@link boxChars}, which all take 3 bytes. */
priv fn boxChar(chars: String, n: Size): UInt do
    val (cp, used) = unicode.decode(chars, n * 3)
    ret cp

/**
 * Draws a frame, inside the rectangle's edge.
 * @param kind the lines to use
 * @param title text in the top edge; "" for none
 * @param style how the frame and title look
 */
fn box(s: Screen, col: Int, row: Int, cols: Int, rows: Int, kind: BoxKind = :single, title: String = "", style: Style = PLAIN) do
    if not (cols < 2 or rows < 2) do
        val chars = boxChars(kind)
        val right = col + cols - 1
        val bottom = row + rows - 1
        fill(s, col + 1, row, cols - 2, 1, boxChar(chars, 0), style)
        fill(s, col + 1, bottom, cols - 2, 1, boxChar(chars, 0), style)
        fill(s, col, row + 1, 1, rows - 2, boxChar(chars, 1), style)
        fill(s, right, row + 1, 1, rows - 2, boxChar(chars, 1), style)
        put(s, col, row, boxChar(chars, 2), style)
        put(s, right, row, boxChar(chars, 3), style)
        put(s, col, bottom, boxChar(chars, 4), style)
        put(s, right, bottom, boxChar(chars, 5), style)
        if title.len > 0 and cols > 6 do
            val room = (cols - 6) as Size
            val shown = if displayWidth(title) > room do trimEnd(fit(title, room)) else title
            text(s, col + 2, row, " ${shown} ", style)

/** Shows the cursor at a cell after {@link render}, as for a text field. */
fn placeCursor(s: Screen, col: Int, row: Int) do
    s.cursorCol = col
    s.cursorRow = row

/** Leaves the cursor hidden after {@link render}. */
fn removeCursor(s: Screen) do
    s.cursorCol = -1

/**
 * The escape sequences that bring the terminal up to date with `s`, and marks them as done. {@link render} writes
 * them; this is for testing what a program draws.
 */
fn frame(s: Screen): String do
    val b = stringBuilder(256)
    if s.stale do
        b.append("\x1b[0m\x1b[2J")
    var style = PLAIN
    var styleKnown = false
    var atCol = -1
    var atRow = -1
    for row in 0..<s.rows do
        for col in 0..<s.cols do
            val i = indexOf(s, col, row)
            val cell = s.cells[i]
            val blank = cell.ch == 32 and cell.style == PLAIN
            if cell.ch != 0 and (if s.stale do not blank else cell != s.shown[i]) do
                if atCol != col or atRow != row do
                    b.append("\x1b[${row + 1};${col + 1}H")
                if not styleKnown or cell.style != style do
                    appendStyle(b, cell.style)
                    style = cell.style
                    styleKnown = true
                unicode.appendCodePoint(b, cell.ch)
                atCol = col + charWidth(cell.ch)
                atRow = row
            s.shown[i] = cell
    b.append("\x1b[0m") if styleKnown and style != PLAIN
    s.stale = false
    if s.cursorCol >= 0 do
        b.append("\x1b[${s.cursorRow + 1};${s.cursorCol + 1}H")
        b.append("\x1b[?25h") if not s.cursorShown
        s.cursorShown = true
    else if s.cursorShown do
        b.append("\x1b[?25l")
        s.cursorShown = false
    ret b.build()

/** Shows what was drawn on `s`, writing only the cells that changed. */
fn render(s: Screen) do
    pool do
        emit(frame(s))

// ---------------------------------------------------------------------------------------------------
// Prompts: these work line by line, without full-screen mode

/** Turns raw mode on if it can be and isn't. @return whether it must be turned off again */
priv fn startPrompt(): Bool do
    ret false if rawActive or isatty(STDIN_FILENO) != 1
    try enterRaw() catch e do
        ret false
    ret true

/** Reads a line key by key, with Backspace. @return the line; null at the end of the input, or for Esc or Ctrl-D */
priv fn readText(echo: Bool): String? do
    val b = stringBuilder(64)
    var got = false
    while true do
        val key = readKey()
        ret (if got do b.build() else null) if key == null
        val k = key
        got = true
        if k.name == :enter do
            ret b.build()
        else if k.name == :escape or isCtrl(k, 'd') do
            ret null
        else if k.name == :backspace and b.len > 0 do
            var start = b.len - 1
            while start > 0 and (b.buffer[start] as UChar) & 0xC0 == 0x80 do
                start -= 1
            if echo do
                val (cp, n) = unicode.decode(b.build(), start)
                emit(repeat("\x08 \x08", charWidth(cp) as Size))
            b.len = start
        else if k.name == :char and not k.ctrl and not k.alt do
            unicode.appendCodePoint(b, k.ch)
            emit(keyText(k)) if echo

/** Writes the prompt, reads a line, and puts the terminal back. */
priv fn prompted(question: String, echo: Bool): String? do
    emit(question)
    val started = startPrompt()
    val line = readText(echo)
    emit("\n") if isatty(STDIN_FILENO) == 1
    leaveRaw() if started
    ret line

/**
 * Asks for a line of text: `val name = term.ask("Name: ") ?? ""`.
 * @param question what to show before the answer; put a space at its end
 * @return the answer; null when it was cancelled with Esc or Ctrl-D, or at the end of the input
 */
fn ask(question: String): String? = prompted(question, true)

/**
 * Asks for a secret, without showing it.
 * @param question what to show before the answer
 * @return the answer; null when it was cancelled with Esc or Ctrl-D, or at the end of the input
 */
fn password(question: String): String? = prompted(question, false)

/**
 * Asks a yes or no question and waits for y or n.
 * @param question what to ask; " [y/N] " is added
 * @param otherwise the answer for Enter, Esc and the end of the input
 * @return the answer
 */
fn confirm(question: String, otherwise: Bool = false): Bool do
    emit(concat(question, if otherwise do " [Y/n] " else " [y/N] "))
    val started = startPrompt()
    var answer = otherwise
    while true do
        val key = readKey()
        break if key == null
        val k = key
        if isChar(k, 'y') or isChar(k, 'Y') do
            answer = true
            break
        if isChar(k, 'n') or isChar(k, 'N') do
            answer = false
            break
        break if k.name == :enter or k.name == :escape
    emit(if answer do "yes\n" else "no\n")
    leaveRaw() if started
    ret answer

/** Draws the rows of a {@link select} from `first`, and leaves the cursor below them. */
priv fn drawOptions(title: String, options: String[], first: Size, count: Size, current: Size) do
    val b = stringBuilder(256)
    b.append(bold(title))
    b.append("\n")
    for i in first..<first + count do
        b.append("\x1b[2K")
        if i == current do
            b.append(styled(concat("> ", options[i]), Style { reverse = true }))
        else do
            b.append(concat("  ", options[i]))
        b.append("\n")
    emit(b.build())

/**
 * Lets the user pick from a list with the arrow keys, Home, End and Enter. A list taller than the terminal scrolls.
 *
 *     if val i = term.select("Colour", names) do
 *         io.log(names[i])
 *
 * @param title what to show above the list
 * @param options what to pick from
 * @param start the option selected at first
 * @return the index of the option picked; null for Esc, q or the end of the input, and when there are no options
 */
fn select(title: String, options: String[], start: Size = 0): Size? do
    ret null if options.len == 0
    val started = startPrompt()
    val wasHidden = cursorHidden
    hideCursor()
    val room = size().rows - 2
    val count = if room < 1 do 1 else if room as Size < options.len do room as Size else options.len
    var current = if start < options.len do start else 0
    var first: Size = 0
    var picked: Size? = null
    while true do
        if current < first do
            first = current
        if current >= first + count do
            first = current + 1 - count
        pool do
            drawOptions(title, options, first, count, current)
        val key = readKey()
        break if key == null
        val k = key
        if k.name == :enter do
            picked = current
            break
        break if k.name == :escape or isChar(k, 'q')
        if k.name == :up or isChar(k, 'k') do
            current = if current == 0 do options.len - 1 else current - 1
        else if k.name == :down or isChar(k, 'j') do
            current = if current + 1 == options.len do 0 else current + 1
        else if k.name == :home do
            current = 0
        else if k.name == :end do
            current = options.len - 1
        moveUp((count + 1) as Int)
    moveUp((count + 1) as Int)
    emit("\r")
    clearBelow()
    showCursor() if not wasHidden
    leaveRaw() if started
    ret picked

// ---------------------------------------------------------------------------------------------------
// Progress

/** A bar of `width` columns, filled as far as `done` of `total` gets: `████░░░░`. */
fn progressBar(done: Size, total: Size, width: Size): String do
    val part = if total == 0 or done >= total do width else done * width / total
    ret concat(repeat("█", part), repeat("░", width - part))

/**
 * Shows progress on the current row, replacing what the last call showed; at the end it moves to the next row.
 * With output that isn't a terminal, it only writes the last one.
 *
 *     for i in 0..=files.len do
 *         term.progress("copying", i, files.len)
 */
fn progress(label: String, done: Size, total: Size) do
    val finished = done >= total
    if isTerminal() or finished do
        val percent = if total == 0 or finished do 100 else done * 100 / total
        pool do
            val line = "${label} ${progressBar(done, total, 30)} ${percent}%"
            emit(if isTerminal() do concat("\r\x1b[2K", line) else line)
            emit("\n") if finished

priv val SPINNER = "⠋⠙⠹⠸⠼⠴⠦⠧⠇⠏"

/** One frame of a spinner: show it with a growing `tick`, about ten times a second. */
fn spinner(tick: Size): String do
    val i = (tick % 10) * 3
    ret SPINNER[i..<i + 3]
