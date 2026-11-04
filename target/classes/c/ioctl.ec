// sys/ioctl.h: device control, here for the size of a terminal. APR has nothing for this. See core:term.ec.
use "sys/ioctl.h"

// A terminal's size in characters, and in pixels when it says.
declare struct winsize as
    ws_row: UShort
    ws_col: UShort
    ws_xpixel: UShort
    ws_ypixel: UShort

// Fills in a winsize.
declare val TIOCGWINSZ as ULong

// -1 on failure.
declare fn ioctl as (fd: Int, request: ULong, ...): Int
