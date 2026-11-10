#!/usr/bin/env python3
"""Checks that `declare fn` bindings match their C headers.

For every `declare fn` in the given .ec files (except C variadic ones), writes an eclang wrapper with the same parameters that calls the
C function, then compiles the program with gcc -Werror: a parameter or return type that doesn't fit the C
declaration (wrong pointer depth, wrong struct, wrong argument count) fails the build.

The wrappers of a file are written in a file of the same module (`module "name"`), so they see its names, and each
is referenced from a global, so the compiler doesn't leave it out of the C as unused.

With --fields, it also checks the fields of each `declare rec` (and `declare struct`) of the files: a wrapper per
record takes `addrof r.field` of each into a pointer of the field's eclang type, which the C turns into a
`_Static_assert` that the field has that type in C (a C enum may be an Int). Bit-fields, `T[N]` arrays (which eclang reads through a cast) and function pointers aren't checked.

Usage: tools/check-bindings.py [--fields] OUT_DIR file.ec...   (paths as `use` would take them, e.g. core:apr/pools.ec)
"""
import os, re, subprocess, sys

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
RESOURCES = os.path.join(ROOT, "src", "main", "resources")
DECLARE = re.compile(r'^(?:priv )?declare fn (\w+)(?: extern "\w+")? as \((.*)\)(?:\s*(?::|=>)\s*(.+?))?\s*$')
MODULE = re.compile(r'^module "([^"]+)"')


def split_params(text):
    """Splits a parameter list at top-level commas (function types and default values can have commas inside brackets)."""
    parts, depth, current = [], 0, ""
    for ch in text:
        depth += ch in "({["
        depth -= ch in ")}]"
        if ch == "," and depth == 0:
            parts.append(current.strip())
            current = ""
        else:
            current += ch
    if current.strip():
        parts.append(current.strip())
    return parts


def strip_default(param):
    """`x: T = value` -> `x: T`: the wrapper takes every parameter (a C function's default is filled in at the call)."""
    depth = 0
    for i, ch in enumerate(param):
        depth += ch in "({["
        depth -= ch in ")}]"
        if ch == "=" and depth == 0 and param[i + 1:i + 2] != ">":
            return param[:i].strip()
    return param


def source(path):
    with open(os.path.join(RESOURCES, path[len("core:"):]) if path.startswith("core:") else path) as f:
        return f.readlines()


def module_of(lines):
    return next((m.group(1) for m in map(MODULE.match, lines) if m), None)


def wrappers(lines):
    """Yields (wrapper name, eclang source) for each non-variadic `declare fn` in [lines]."""
    for i, line in enumerate(lines):
        m = DECLARE.match(line.strip())
        if not m:
            continue
        name, params, returns = m.group(1), [strip_default(p) for p in split_params(m.group(2))], m.group(3)
        if "..." in params:
            continue  # A C variadic function can't be forwarded to (gcc checks its format/sentinel arguments).
        # `out x: T` (C only writes it) and `mut x: T` are passed on as `mut x`; `escaping x: T` and `kept x: T` as x.
        words = [p.split()[0] if p.split()[0] in ("out", "mut", "escaping", "kept") else "" for p in params]
        plain = [p.split(None, 1)[1] if w else p for p, w in zip(params, words)]
        names = [p.split(":", 1)[0].strip() for p in plain]
        wrapper = f"check_{name}_{i}"  # numbered: overloads of one name each get a wrapper
        forwarded = [f"mut {p}" if w in ("out", "mut") else p for p, w in zip(plain, words)]
        # A function type is a C function pointer only in a `declare` or `export fn`: elsewhere it is an eclang function value.
        written = m.group(2) + " " + (returns or "")
        exported = "export " if "=>" in written or "fn (" in written else ""
        signature = f"{exported}fn {wrapper}({', '.join(forwarded)})" + (f": {returns}" if returns else "")
        call = f"{name}({', '.join(f'mut {n}' if w in ('out', 'mut') else n for n, w in zip(names, words))})"
        yield wrapper, f"{signature} = {call}\n" if returns else f"{signature} do\n    {call}\n"


RECORD = re.compile(r'^(?:priv )?declare (?:mut )?(?:union )?(?:rec|struct|union) (\w+)(?: extern "\w+")? as\s*$')
FIELD = re.compile(r'^\s+(\w+)(?: extern "\w+")?: (.+?)\s*$')


def field_checks(lines):
    """Yields (wrapper name, eclang source) per `declare rec` with fields in [lines]: see --fields."""
    i = 0
    while i < len(lines):
        m = RECORD.match(lines[i].rstrip())
        i += 1
        if not m:
            continue
        name, body = m.group(1), []
        while i < len(lines) and (lines[i].startswith((" ", "\t")) or not lines[i].strip()):
            f = FIELD.match(lines[i].rstrip())
            i += 1
            if not f or f.group(1) in ("is",):
                continue
            t = f.group(2)
            if re.search(r"\[\d+\]$", t) or re.search(r" : \d+$", t) or "=>" in t:
                continue  # A T[N] (read through a cast), a bit-field (which has no address) or a C function pointer (an eclang function value here).
            body.append(f"    val f{len(body)}: @({t}) = addrof r.{f.group(1)}\n")
        if body:
            wrapper = f"fields_{name}_{i}"
            yield wrapper, f"fn {wrapper}(r: @{name}) do\n" + "".join(body)


def main():
    fields = "--fields" in sys.argv
    args = [a for a in sys.argv[1:] if a != "--fields"]
    out, files = args[0], args[1:]
    os.makedirs(out, exist_ok=True)
    program = os.path.join(out, "bindings.ec")
    uses = []
    for i, p in enumerate(files):
        lines = source(p)
        module = module_of(lines)
        part = os.path.join(out, f"bindings_{i}.ec")
        uses.append(part)
        with open(part, "w") as f:
            if module:
                f.write(f'module "{module}"\n')
            f.write(f'use "{p if p.startswith("core:") else os.path.abspath(p)}"\n\n')
            for wrapper, code in list(wrappers(lines)) + (list(field_checks(lines)) if fields else []):
                f.write(code)
                f.write(f"val ref_{wrapper} = fn {wrapper}\n")
    with open(program, "w") as f:
        f.writelines(f'use "{p}"\n' for p in uses)
        f.write("\nfn main(): Int do\n    ret 0\n")
    c = os.path.join(out, "bindings.c")
    compiler = ["java", "-cp", f"{RESOURCES}:{os.path.join(ROOT, 'target', 'eclang-0.1.0.jar')}", "github.mrh0.eclang.MainKt"]
    subprocess.run(compiler + ["-c", program, "-o", c], check=True)
    # Calls of C functions cast pointer-to-pointer arguments to void* and pointer results to eclang's type, because
    # eclang has no `const`. Those casts would hide wrong bindings here, so remove them, and compile with `const`
    # (and `volatile`) defined away instead: any remaining mismatch is a real one.
    with open(c) as f:
        code = f.read()
    code = re.sub(r"\(\(void\*\)\((\w+)\)\)", r"\1", code)
    code = re.sub(r"^(\s*return )\(\([^()]+\)\((.*)\)\);$", r"\1\2;", code, flags=re.M)
    # --fields: each `T* fN = &r->field;` (also when eclang reads the field through a cast or a NULL check) becomes an
    # assertion that the field is a T in C. eclang can't write some C types, so these are taken as the same: an
    # enum and `int` (eclang's Int), `long long` and `long` (Long), and `signed char` and `char` (Char).
    def field_assert(m):
        ptr, place = m.group(1).strip(), re.search(r"r->[\w.]+", m.group(2)).group(0)
        target = ptr[:-1].strip()
        def same(a, b):
            return f"__builtin_types_compatible_p({a}, {b})"
        field = f"__typeof__({place})"
        alike = " || ".join([
            same(f"__typeof__(&{place})", ptr),
            f"(__builtin_classify_type({field}) == 3 && sizeof({place}) == sizeof(int) && {same(target, 'int')})",
            f"({same(target, 'long')} && {same(field, 'long long')})",
            f"({same(target, 'unsigned long')} && {same(field, 'unsigned long long')})",
            f"({same(target, 'char')} && {same(field, 'signed char')} && (char)-1 < 0)",
        ])
        return f"\t_Static_assert({alike}, \"the field {place[3:]} isn't a {target} in C\");"
    code = re.sub(r"^\t(.+?\*)\s*f\d+ = &(.*r->[\w.]+.*);$", field_assert, code, flags=re.M)
    with open(c, "w") as f:
        f.write(code)
    # Unused parameters are expected: some functions are macros that expand to nothing. Unused variables too: with
    # `const` defined away, the atoms (`static char* const`) a program doesn't use are unused statics.
    subprocess.run(["gcc", "-Wall", "-Wextra", "-Wconversion", "-Werror", "-Wno-unused-parameter", "-Wno-unused-variable", "-Dconst=", "-Dvolatile=", "-c", c, "-o", os.path.join(out, "bindings.o"),
                    "-I/usr/include/apr-1.0"], check=True)
    print(f"{len(files)} file(s): every declare fn matches its C declaration.")


main()
