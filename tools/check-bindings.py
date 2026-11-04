#!/usr/bin/env python3
"""Checks that `declare fn` bindings match their C headers.

For every `declare fn` in the given .ec files (except C variadic ones), writes an eclang wrapper with the same parameters that calls the
C function, then compiles the program with gcc -Werror: a parameter or return type that doesn't fit the C
declaration (wrong pointer depth, wrong struct, wrong argument count) fails the build.

The wrappers of a file are written in a file of the same module (`module "name"`), so they see its names, and each
is referenced from a global, so the compiler doesn't leave it out of the C as unused.

Usage: tools/check-bindings.py OUT_DIR file.ec...   (paths as `use` would take them, e.g. core:apr/pools.ec)
"""
import os, re, subprocess, sys

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
RESOURCES = os.path.join(ROOT, "src", "main", "resources")
DECLARE = re.compile(r'^(?:priv )?declare fn (\w+)(?: extern "\w+")? as \((.*)\)(?:\s*(?::|=>)\s*(.+?))?\s*$')
MODULE = re.compile(r'^module "([^"]+)"')


def split_params(text):
    """Splits a parameter list at top-level commas (function types have commas inside parentheses)."""
    parts, depth, current = [], 0, ""
    for ch in text:
        depth += ch == "("
        depth -= ch == ")"
        if ch == "," and depth == 0:
            parts.append(current.strip())
            current = ""
        else:
            current += ch
    if current.strip():
        parts.append(current.strip())
    return parts


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
        name, params, returns = m.group(1), split_params(m.group(2)), m.group(3)
        if "..." in params:
            continue  # A C variadic function can't be forwarded to (gcc checks its format/sentinel arguments).
        names = [p.split(":", 1)[0].strip() for p in params]
        wrapper = f"check_{name}_{i}"  # numbered: overloads of one name each get a wrapper
        signature = f"fn {wrapper}({', '.join(params)})" + (f": {returns}" if returns else "")
        call = f"{name}({', '.join(names)})"
        yield wrapper, f"{signature} = {call}\n" if returns else f"{signature} do\n    {call}\n"


def main():
    out, files = sys.argv[1], sys.argv[2:]
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
            for wrapper, code in wrappers(lines):
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
    with open(c, "w") as f:
        f.write(code)
    # Unused parameters are expected: some functions are macros that expand to nothing. Unused variables too: with
    # `const` defined away, the atoms (`static char* const`) a program doesn't use are unused statics.
    subprocess.run(["gcc", "-Wall", "-Wextra", "-Wconversion", "-Werror", "-Wno-unused-parameter", "-Wno-unused-variable", "-Dconst=", "-Dvolatile=", "-c", c, "-o", os.path.join(out, "bindings.o"),
                    "-I/usr/include/apr-1.0"], check=True)
    print(f"{len(files)} file(s): every declare fn matches its C declaration.")


main()
