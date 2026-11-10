#!/usr/bin/env python3
"""Generates constants.ec, the #define constants of the SQLite bindings, from sqlite3.h.

The functions, types and helpers are written by hand in sqlite.ec, because what C's types don't say (can a pointer be
NULL? does the function fill it in?) needs a decision per function. The constants need none, and there are hundreds
of them (result codes, open flags, configuration options, limits), so they come from the installed header: every
#define starting with a prefix below, with the eclang type of what it expands to, which a small C program asks gcc
with `_Generic` (`SQLITE_IOERR_READ` is `(SQLITE_IOERR | (1<<8))`, an Int; `SQLITE_VERSION` is a CString).

Usage: bindings/sqlite/generate.py [INCLUDE_DIR] > bindings/sqlite/constants.ec
INCLUDE_DIR is the directory holding sqlite3.h; without it, gcc's own search path is used.
"""
import os
import re
import subprocess
import sys
import tempfile

# The primary result codes. Each extended code starts with the name of its primary code (SQLITE_IOERR_READ).
PRIMARY = """OK ERROR INTERNAL PERM ABORT BUSY LOCKED NOMEM READONLY INTERRUPT IOERR CORRUPT NOTFOUND FULL CANTOPEN
PROTOCOL EMPTY SCHEMA TOOBIG CONSTRAINT MISMATCH MISUSE NOLFS AUTH FORMAT RANGE NOTADB NOTICE WARNING ROW DONE""".split()

# #define constants: (regex of names, heading). The first that matches a name decides its heading.
MACROS = [
    (r"SQLITE_(VERSION|VERSION_NUMBER|SOURCE_ID)", "The version of sqlite3.h (sqlite3_libversion() is that of the library)"),
    (r"SQLITE_(%s)" % "|".join(PRIMARY), "Primary result codes: what most functions return (sqlite3_errstr says what each means)"),
    (r"SQLITE_(%s)_\w+" % "|".join(PRIMARY), "Extended result codes, the primary code | (n << 8): sqlite3_extended_errcode, or every result after sqlite3_extended_result_codes(db, 1)"),
    (r"SQLITE_OPEN_\w+", "Flags of sqlite3_open_v2"),
    (r"SQLITE_PREPARE_\w+", "Flags of sqlite3_prepare_v3"),
    (r"SQLITE_(INTEGER|FLOAT|BLOB|NULL|TEXT)|SQLITE3_TEXT", "Types of values: sqlite3_column_type, sqlite3_value_type"),
    (r"SQLITE_(UTF8|UTF16LE|UTF16BE|UTF16|ANY|UTF16_ALIGNED)", "Text encodings"),
    (r"SQLITE_(DETERMINISTIC|DIRECTONLY|SUBTYPE|INNOCUOUS|RESULT_SUBTYPE|SELFORDER1)", "Flags of sqlite3_create_function_v2, combined with a text encoding"),
    (r"SQLITE_CONFIG_\w+", "Options of sqlite3_config"),
    (r"SQLITE_DBCONFIG_\w+", "Options of sqlite3_db_config"),
    (r"SQLITE_LIMIT_\w+", "Limits of sqlite3_limit"),
    (r"SQLITE_STATUS_\w+", "Counters of sqlite3_status64"),
    (r"SQLITE_DBSTATUS_\w+", "Counters of sqlite3_db_status"),
    (r"SQLITE_STMTSTATUS_\w+", "Counters of sqlite3_stmt_status"),
    (r"SQLITE_CHECKPOINT_\w+", "Modes of sqlite3_wal_checkpoint_v2"),
    (r"SQLITE_TXN_\w+", "Results of sqlite3_txn_state"),
    (r"SQLITE_TRACE_\w+", "Events of sqlite3_trace_v2"),
    (r"SQLITE_SERIALIZE_\w+", "Flags of sqlite3_serialize"),
    (r"SQLITE_DESERIALIZE_\w+", "Flags of sqlite3_deserialize"),
]

C_TYPES = {"int": "Int", "unsigned": "UInt", "long": "Long", "unsigned long": "ULong", "long long": "Long",
           "unsigned long long": "ULong", "double": "Double", "char*": "CString"}


def gcc(args, source):
    with tempfile.NamedTemporaryFile("w", suffix=".c", delete=False) as f:
        f.write(source)
    try:
        return subprocess.run(["gcc"] + args + [f.name], capture_output=True, text=True, check=True).stdout
    finally:
        os.unlink(f.name)


def main():
    include = ["-I" + sys.argv[1]] if len(sys.argv) > 1 else []
    header = "#include <sqlite3.h>\n"
    defines = gcc(["-E", "-dM"] + include, header)

    macros = {}  # name -> heading index
    for line in defines.splitlines():
        m = re.match(r"#define (\w+)(\(?)", line)
        if not m or m.group(2):
            continue
        for i, (pattern, _) in enumerate(MACROS):
            if re.fullmatch(pattern, m.group(1)):
                macros[m.group(1)] = i
                break

    # The type of each macro, from gcc: a program printing `_Generic((NAME), ...)` per macro.
    names = sorted(macros, key=lambda n: (macros[n], n))
    cases = ", ".join(f'{c}: "{e}"' for c, e in C_TYPES.items())
    probe = header + "#include <stdio.h>\nint main(void) {\n" + "".join(
        f'    printf("%s\\n", _Generic(({n}), {cases}, default: "?"));\n' for n in names) + "    return 0;\n}\n"
    with tempfile.TemporaryDirectory() as tmp:
        exe = os.path.join(tmp, "probe")
        gcc(include + ["-w", "-o", exe], probe)
        types = dict(zip(names, subprocess.run([exe], capture_output=True, text=True, check=True).stdout.split()))
    version = re.search(r'#define SQLITE_VERSION "([^"]+)"', defines).group(1)

    out = []
    w = out.append
    w("/**")
    w(f" * Constants of the SQLite bindings: SQLite {version}.")
    w(" *")
    w(" * Generated from sqlite3.h by bindings/sqlite/generate.py; run it again rather than changing this file.")
    w(" */")
    w('use "sqlite3.h"')
    for i, (_, heading) in enumerate(MACROS):
        group = [n for n in names if macros[n] == i and types[n] != "?"]
        if not group:
            continue
        w("")
        w(f"// {heading}")
        for n in group:
            w(f"declare val {n} as {types[n]}")
    print("\n".join(out))


main()
