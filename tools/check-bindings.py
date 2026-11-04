#!/usr/bin/env python3
"""Checks that `declare fn` bindings match their C headers.

For every `declare fn` in the given .ec files (except C variadic ones), writes an eclang wrapper with the same parameters that calls the
C function, then compiles the program with gcc -Werror: a parameter or return type that doesn't fit the C
declaration (wrong pointer depth, wrong struct, wrong argument count) fails the build.

Usage: tools/check-bindings.py OUT_DIR file.ec...   (paths as `use` would take them, e.g. core:apr/pools.ec)
"""
import os, re, subprocess, sys

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
RESOURCES = os.path.join(ROOT, "src", "main", "resources")
DECLARE = re.compile(r'^declare fn (\w+)(?: extern "\w+")? as \((.*)\)(?:\s*(?::|=>)\s*(.+?))?\s*$')


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


def wrappers(path):
    source = path[len("core:"):] if path.startswith("core:") else path
    with open(os.path.join(RESOURCES, source) if path.startswith("core:") else source) as f:
        for line in f:
            m = DECLARE.match(line.strip())
            if not m:
                continue
            name, params, returns = m.group(1), split_params(m.group(2)), m.group(3)
            if "..." in params:
                continue  # A C variadic function can't be forwarded to (gcc checks its format/sentinel arguments).
            fixed = [p for p in params if p != "..."]
            names = [p.split(":", 1)[0].strip() for p in fixed]
            signature = f"fn check_{name}({', '.join(fixed)})" + (f": {returns}" if returns else "")
            call = f"{name}({', '.join(names)})"
            yield f"{signature} = {call}\n" if returns else f"{signature} do\n    {call}\n"


def main():
    out, files = sys.argv[1], sys.argv[2:]
    os.makedirs(out, exist_ok=True)
    program = os.path.join(out, "bindings.ec")
    with open(program, "w") as f:
        f.writelines(f'use "{p}"\n' for p in files)
        f.write("\n")
        for p in files:
            f.writelines(wrappers(p))
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
    # Unused parameters are expected: some functions are macros that expand to nothing.
    subprocess.run(["gcc", "-Wall", "-Wextra", "-Wconversion", "-Werror", "-Wno-unused-parameter", "-Dconst=", "-Dvolatile=", "-c", c, "-o", os.path.join(out, "bindings.o"),
                    "-I/usr/include/apr-1.0"], check=True)
    print(f"{len(files)} file(s): every declare fn matches its C declaration.")


main()
