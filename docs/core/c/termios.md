# `termios.ec`

## Types

### <a id="tcflag_t"></a>`tcflag_t`

```ec
declare type tcflag_t as UInt
```

### <a id="termios"></a>`termios`

```ec
declare struct termios
```

**Fields**

- <a id="termios.c_iflag"></a>`c_iflag: tcflag_t`
- <a id="termios.c_oflag"></a>`c_oflag: tcflag_t`
- <a id="termios.c_cflag"></a>`c_cflag: tcflag_t`
- <a id="termios.c_lflag"></a>`c_lflag: tcflag_t`

## Values

### <a id="ECHO"></a>`ECHO`

```ec
declare val ECHO as tcflag_t
```

### <a id="ICANON"></a>`ICANON`

```ec
declare val ICANON as tcflag_t
```

### <a id="ISIG"></a>`ISIG`

```ec
declare val ISIG as tcflag_t
```

### <a id="OPOST"></a>`OPOST`

```ec
declare val OPOST as tcflag_t
```

### <a id="TCSANOW"></a>`TCSANOW`

```ec
declare val TCSANOW as Int
```

### <a id="TCSADRAIN"></a>`TCSADRAIN`

```ec
declare val TCSADRAIN as Int
```

### <a id="TCSAFLUSH"></a>`TCSAFLUSH`

```ec
declare val TCSAFLUSH as Int
```

## Functions

### <a id="tcgetattr"></a>`tcgetattr`

```ec
fn tcgetattr(fd: Int, settings: @termios): Int
```

*Binds the C function `tcgetattr`.*

### <a id="tcsetattr"></a>`tcsetattr`

```ec
fn tcsetattr(fd: Int, when_: Int, settings: @termios): Int
```

*Binds the C function `tcsetattr`.*

### <a id="cfmakeraw"></a>`cfmakeraw`

```ec
fn cfmakeraw(settings: @termios)
```

*Binds the C function `cfmakeraw`.*
