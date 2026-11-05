# `ioctl.ec`

## Types

### <a id="winsize"></a>`winsize`

```ec
declare struct winsize
```

**Fields**

- <a id="winsize.ws_row"></a>`ws_row: UShort`
- <a id="winsize.ws_col"></a>`ws_col: UShort`
- <a id="winsize.ws_xpixel"></a>`ws_xpixel: UShort`
- <a id="winsize.ws_ypixel"></a>`ws_ypixel: UShort`

## Values

### <a id="TIOCGWINSZ"></a>`TIOCGWINSZ`

```ec
declare val TIOCGWINSZ as ULong
```

## Functions

### <a id="ioctl"></a>`ioctl`

```ec
fn ioctl(fd: Int, request: ULong, ...): Int
```

*Binds the C function `ioctl`.*
