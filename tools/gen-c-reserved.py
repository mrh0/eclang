#!/usr/bin/env python3
"""Regenerates src/main/resources/c-reserved.txt: the file-scope names (functions, variables, typedefs, enum
constants, macros, tags) that the headers every generated C file includes declare, and c-reserved-headers.txt: what each
other header the core: library can include adds to those, for the programs that include it. eclang renames user names
that would clash with them (see CompileData.isCReserved). Names starting with `_` or `apr_`/`APR_` are reserved by rule,
so they aren't listed.
Usage: tools/gen-c-reserved.py   (needs gcc and the APR headers)"""
import glob, os, re, subprocess, tempfile

HEADERS = ["stdio.h", "stdlib.h", "string.h", "math.h", "stdbool.h", "stddef.h", "stdint.h", "limits.h", "stdalign.h", "signal.h", "sys/resource.h", "apr_general.h", "apr_pools.h"]
INC = ["-I/usr/include/apr-1.0"]
resources = os.path.join(os.path.dirname(os.path.dirname(os.path.abspath(__file__))), "src/main/resources")

def declared(d, headers):
    """The names [headers], included in this order, declare at file scope."""
    src = os.path.join(d, "h.c")
    open(src, "w").write("".join(f"#include <{h}>\n" for h in headers))
    names = set()
    # Functions: gcc writes every prototype it sees.
    aux = os.path.join(d, "aux.txt")
    subprocess.run(["gcc", "-c", src, "-o", os.path.join(d, "h.o"), "-aux-info", aux] + INC, check=True)
    for line in open(aux):
        m = re.search(r"\*/\s*(?:extern\s+)?.*?\b([A-Za-z_]\w*)\s*\(", line)
        if m: names.add(m.group(1))
    # Macros.
    for line in subprocess.run(["gcc", "-E", "-dM", src] + INC, capture_output=True, text=True, check=True).stdout.splitlines():
        names.add(re.match(r"#define (\w+)", line).group(1))
    # Typedefs, extern variables and enum constants, from the preprocessed text split into top-level declarations.
    text = subprocess.run(["gcc", "-E", "-P", src] + INC, capture_output=True, text=True, check=True).stdout
    for body in re.findall(r"enum\s*\w*\s*\{([^}]*)\}", text):
        names.update(re.findall(r"(?:^|,)\s*([A-Za-z_]\w*)", body))
    # Tags: a record is a C struct of the same name.
    names.update(re.findall(r"\b(?:struct|union|enum)\s+([A-Za-z_]\w*)", text))
    depth, decl = 0, ""
    for ch in text:
        if ch == "{": depth += 1
        elif ch == "}": depth -= 1
        elif depth == 0:
            if ch == ";":
                decl = re.sub(r"__attribute__\s*\(\(.*?\)\)|__asm__\s*\(.*?\)", "", decl.strip())
                if decl.startswith(("typedef", "extern")) and "(" not in decl:
                    m = re.search(r"([A-Za-z_]\w*)\s*(\[[^\]]*\])*\s*$", decl)
                    if m: names.add(m.group(1))
                elif decl.startswith("typedef"):
                    m = re.search(r"\(\s*\*\s*([A-Za-z_]\w*)\s*\)", decl)
                    if m: names.add(m.group(1))
                decl = ""
            else: decl += ch
    return {n for n in names if not n.startswith(("_", "apr_", "APR_"))}

def bundled():
    """The headers the core: library can include. Its bundled C includes none (see vendor/README.md)."""
    found = set()
    for path in glob.glob(os.path.join(resources, "**", "*.ec"), recursive=True):
        found.update(re.findall(r'^\s*(?:priv\s+)?use "([^":]+\.h)"', open(path).read(), re.M))
    return sorted(found - set(HEADERS))

with tempfile.TemporaryDirectory() as d:
    base = declared(d, HEADERS)
    open(os.path.join(resources, "c-reserved.txt"), "w").write("\n".join(sorted(base)) + "\n")
    print(f"{len(base)} names in every program")
    with open(os.path.join(resources, "c-reserved-headers.txt"), "w") as out:
        for header in bundled():
            names = declared(d, HEADERS + [header]) - base
            out.write(f"{header}: {' '.join(sorted(names))}\n")
            print(f"{header}: {len(names)} more")
