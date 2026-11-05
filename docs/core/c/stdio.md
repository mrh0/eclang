# `stdio.ec`

## Types

### <a id="FILE"></a>`FILE`

```ec
declare rec FILE
```

## Values

### <a id="stdin"></a>`stdin`

```ec
declare var stdin as @FILE
```

### <a id="stdout"></a>`stdout`

```ec
declare var stdout as @FILE
```

### <a id="stderr"></a>`stderr`

```ec
declare var stderr as @FILE
```

### <a id="EOF"></a>`EOF`

```ec
declare val EOF as Int
```

### <a id="BUFSIZ"></a>`BUFSIZ`

```ec
declare val BUFSIZ as Int
```

### <a id="_IOFBF"></a>`_IOFBF`

```ec
declare val _IOFBF as Int
```

### <a id="_IONBF"></a>`_IONBF`

```ec
declare val _IONBF as Int
```

## Functions

### <a id="printf"></a>`printf`

```ec
fn printf(format: CString, ...): Int
```

*Binds the C function `printf`.*

### <a id="fprintf"></a>`fprintf`

```ec
fn fprintf(stream: @FILE, format: CString, ...): Int
```

*Binds the C function `fprintf`.*

### <a id="snprintf"></a>`snprintf`

```ec
fn snprintf(buffer: CString, size: Size, format: CString, ...): Int
```

*Binds the C function `snprintf`.*

### <a id="puts"></a>`puts`

```ec
fn puts(s: CString): Int
```

*Binds the C function `puts`.*

### <a id="putchar"></a>`putchar`

```ec
fn putchar(c: Int): Int
```

*Binds the C function `putchar`.*

### <a id="fputs"></a>`fputs`

```ec
fn fputs(s: CString, stream: @FILE): Int
```

*Binds the C function `fputs`.*

### <a id="fputc"></a>`fputc`

```ec
fn fputc(c: Int, stream: @FILE): Int
```

*Binds the C function `fputc`.*

### <a id="fwrite"></a>`fwrite`

```ec
fn fwrite(data: @Any, size: Size, count: Size, stream: @FILE): Size
```

*Binds the C function `fwrite`.*

### <a id="fflush"></a>`fflush`

```ec
fn fflush(stream: @FILE?): Int
```

*Binds the C function `fflush`.*

### <a id="setvbuf"></a>`setvbuf`

```ec
fn setvbuf(stream: @FILE, buffer: CString?, mode: Int, size: Size): Int
```

*Binds the C function `setvbuf`.*

### <a id="getchar"></a>`getchar`

```ec
fn getchar(): Int
```

*Binds the C function `getchar`.*

### <a id="fgetc"></a>`fgetc`

```ec
fn fgetc(stream: @FILE): Int
```

*Binds the C function `fgetc`.*

### <a id="fgets"></a>`fgets`

```ec
fn fgets(buffer: CString, size: Int, stream: @FILE): CString?
```

*Binds the C function `fgets`.*

### <a id="fread"></a>`fread`

```ec
fn fread(data: @Any, size: Size, count: Size, stream: @FILE): Size
```

*Binds the C function `fread`.*

### <a id="fopen"></a>`fopen`

```ec
fn fopen(path: CString, mode: CString): @FILE?
```

*Binds the C function `fopen`.*

### <a id="fclose"></a>`fclose`

```ec
fn fclose(stream: @FILE): Int
```

*Binds the C function `fclose`.*

### <a id="feof"></a>`feof`

```ec
fn feof(stream: @FILE): Int
```

*Binds the C function `feof`.*

### <a id="ferror"></a>`ferror`

```ec
fn ferror(stream: @FILE): Int
```

*Binds the C function `ferror`.*

### <a id="perror"></a>`perror`

```ec
fn perror(s: CString)
```

*Binds the C function `perror`.*

### <a id="remove"></a>`remove`

```ec
fn remove(path: CString): Int
```

*Binds the C function `remove`.*

### <a id="rename"></a>`rename`

```ec
fn rename(oldPath: CString, newPath: CString): Int
```

*Binds the C function `rename`.*
