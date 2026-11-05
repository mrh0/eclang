# `signal.ec`

## Values

### <a id="SIGABRT"></a>`SIGABRT`

```ec
declare val SIGABRT as Int
```

### <a id="SIGALRM"></a>`SIGALRM`

```ec
declare val SIGALRM as Int
```

### <a id="SIGBUS"></a>`SIGBUS`

```ec
declare val SIGBUS as Int
```

### <a id="SIGCHLD"></a>`SIGCHLD`

```ec
declare val SIGCHLD as Int
```

### <a id="SIGCONT"></a>`SIGCONT`

```ec
declare val SIGCONT as Int
```

### <a id="SIGFPE"></a>`SIGFPE`

```ec
declare val SIGFPE as Int
```

### <a id="SIGHUP"></a>`SIGHUP`

```ec
declare val SIGHUP as Int
```

### <a id="SIGILL"></a>`SIGILL`

```ec
declare val SIGILL as Int
```

### <a id="SIGINT"></a>`SIGINT`

```ec
declare val SIGINT as Int
```

### <a id="SIGKILL"></a>`SIGKILL`

```ec
declare val SIGKILL as Int
```

### <a id="SIGPIPE"></a>`SIGPIPE`

```ec
declare val SIGPIPE as Int
```

### <a id="SIGQUIT"></a>`SIGQUIT`

```ec
declare val SIGQUIT as Int
```

### <a id="SIGSEGV"></a>`SIGSEGV`

```ec
declare val SIGSEGV as Int
```

### <a id="SIGSTOP"></a>`SIGSTOP`

```ec
declare val SIGSTOP as Int
```

### <a id="SIGTERM"></a>`SIGTERM`

```ec
declare val SIGTERM as Int
```

### <a id="SIGTSTP"></a>`SIGTSTP`

```ec
declare val SIGTSTP as Int
```

### <a id="SIGUSR1"></a>`SIGUSR1`

```ec
declare val SIGUSR1 as Int
```

### <a id="SIGUSR2"></a>`SIGUSR2`

```ec
declare val SIGUSR2 as Int
```

### <a id="SIGWINCH"></a>`SIGWINCH`

```ec
declare val SIGWINCH as Int
```

### <a id="SIG_DFL"></a>`SIG_DFL`

```ec
declare val SIG_DFL as (Int) => None
```

### <a id="SIG_IGN"></a>`SIG_IGN`

```ec
declare val SIG_IGN as (Int) => None
```

## Functions

### <a id="raise"></a>`raise`

```ec
fn raise(sig: Int): Int
```

*Binds the C function `raise`.*
