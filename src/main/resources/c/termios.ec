// termios.h: terminal settings. APR has nothing for these. For the eclang API, see core:term.ec.
use "termios.h"

declare type tcflag_t as UInt

// The modes of a terminal. C's struct has more fields (c_cc, the speeds), which tcgetattr fills in and tcsetattr reads.
declare struct termios as
    c_iflag: tcflag_t
    c_oflag: tcflag_t
    c_cflag: tcflag_t
    c_lflag: tcflag_t

// c_lflag: show what is typed, wait for a whole line, turn Ctrl-C and Ctrl-Z into signals.
declare val ECHO as tcflag_t
declare val ICANON as tcflag_t
declare val ISIG as tcflag_t
// c_oflag: process output, such as turning "\n" into "\r\n".
declare val OPOST as tcflag_t

// When tcsetattr applies: now, once the output is written, or then and also dropping unread input.
declare val TCSANOW as Int
declare val TCSADRAIN as Int
declare val TCSAFLUSH as Int

// 0 on success, -1 on failure (such as when fd isn't a terminal).
declare fn tcgetattr as (fd: Int, settings: @termios): Int
declare fn tcsetattr as (fd: Int, when_: Int, settings: @termios): Int
// Turns off everything that processes input and output (a GNU and BSD extension).
declare fn cfmakeraw as (settings: @termios)
