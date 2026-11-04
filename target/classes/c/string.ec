// string.h: raw memory and C string functions. For eclang Strings, see core:string.ec.
use "string.h"

declare fn memcpy as (dst: @Any, src: @Any, n: Size): @Any
declare fn memmove as (dst: @Any, src: @Any, n: Size): @Any
declare fn memset as (dst: @Any, c: Int, n: Size): @Any
declare fn memcmp as (a: @Any, b: @Any, n: Size): Int
// null when c isn't in the first n bytes.
declare fn memchr as (s: @Any, c: Int, n: Size): @Any?

declare fn strlen as (s: CString): Size
declare fn strcmp as (a: CString, b: CString): Int
declare fn strncmp as (a: CString, b: CString, n: Size): Int
// These return null when nothing is found.
declare fn strchr as (s: CString, c: Int): CString?
declare fn strrchr as (s: CString, c: Int): CString?
declare fn strstr as (haystack: CString, needle: CString): CString?
declare fn strspn as (s: CString, accept: CString): Size
declare fn strcspn as (s: CString, reject: CString): Size
// The message for an errno value.
declare fn strerror as (errnum: Int): CString
