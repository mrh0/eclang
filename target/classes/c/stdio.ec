// stdio.h: C's standard input and output streams.
use "stdio.h"

declare rec FILE

declare var stdin as @FILE
declare var stdout as @FILE
declare var stderr as @FILE

declare val EOF as Int
declare val BUFSIZ as Int

declare fn printf as (format: CString, ...): Int
declare fn fprintf as (stream: @FILE, format: CString, ...): Int
declare fn snprintf as (buffer: CString, size: Size, format: CString, ...): Int
declare fn puts as (s: CString): Int
declare fn putchar as (c: Int): Int
declare fn fputs as (s: CString, stream: @FILE): Int
declare fn fputc as (c: Int, stream: @FILE): Int
declare fn fwrite as (data: @Any, size: Size, count: Size, stream: @FILE): Size
declare fn fflush as (stream: @FILE?): Int

declare fn getchar as (): Int
declare fn fgetc as (stream: @FILE): Int
// Reads one line (with its '\n') into buffer; null at the end of the stream or on an error.
declare fn fgets as (buffer: CString, size: Int, stream: @FILE): CString?
declare fn fread as (data: @Any, size: Size, count: Size, stream: @FILE): Size

// null when the file can't be opened (errno says why).
declare fn fopen as (path: CString, mode: CString): @FILE?
declare fn fclose as (stream: @FILE): Int
declare fn feof as (stream: @FILE): Int
declare fn ferror as (stream: @FILE): Int
declare fn perror as (s: CString)
declare fn remove as (path: CString): Int
declare fn rename as (oldPath: CString, newPath: CString): Int
