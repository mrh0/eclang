// ctype.h: character classes of the current C locale. Each takes a character as an Int (`c as Int`, or EOF)
// and returns non-zero for true. core:string.ec has locale-independent ASCII versions that take a Char.
use "ctype.h"

declare fn isalpha as (c: Int): Int
declare fn isdigit as (c: Int): Int
declare fn isalnum as (c: Int): Int
declare fn isxdigit as (c: Int): Int
declare fn isspace as (c: Int): Int
declare fn isblank as (c: Int): Int
declare fn isupper as (c: Int): Int
declare fn islower as (c: Int): Int
declare fn ispunct as (c: Int): Int
declare fn isprint as (c: Int): Int
declare fn isgraph as (c: Int): Int
declare fn iscntrl as (c: Int): Int
declare fn toupper as (c: Int): Int
declare fn tolower as (c: Int): Int
