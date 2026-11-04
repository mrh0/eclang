use "math.h"
use "stdio.h"
use "stdlib.h"
use "apr_general.h"
use "apr_pools.h"

// stdio
declare fn printf as (format: CString, ...): Int

// math: `%` is only for whole numbers; fmod(a, b) is the remainder of a / b for Doubles.
declare fn fmod as (x: Double, y: Double): Double

// The usual error type for `throws Error`.
rec Error as
    code: Int
    message: String

fn log(value: CString) do
    printf("%s\n"c, value)

fn log(value: String) do
    printf("%.*s\n"c, value.len as Int, value.data)

fn log(value: Atom) do
    printf(":%s\n"c, value)

fn log(value: Bool) do
    printf("%s\n"c, if value do "true"c else "false"c)

fn log(value: Char) do
    printf("%c\n"c, value)

fn log(value: Short) do
    printf("%hd\n"c, value)

fn log(value: Int) do
    printf("%d\n"c, value)

fn log(value: Long) do
    printf("%ld\n"c, value)

fn log(value: UChar) do
    printf("%hhu\n"c, value)

fn log(value: UShort) do
    printf("%hu\n"c, value)

fn log(value: UInt) do
    printf("%u\n"c, value)

fn log(value: ULong) do
    printf("%lu\n"c, value)

fn log(value: Size) do
    printf("%zu\n"c, value)

fn log(value: Float) do
    printf("%g\n"c, value)

fn log(value: Double) do
    printf("%g\n"c, value)
