#!/usr/bin/env bash
# Runs the eclang test suite. Build the compiler first: `mvn clean package`.
#   the ect2 unit tests, the compiler tests (src/main/kotlin/github/mrh0/eclang/CompilerTest.kt), `eclang run`, and a check that the bundled `declare fn` bindings (core:*.ec, core:c/*.ec, core:apr/*.ec) match their C headers
#   tests/pass/NAME.ec  must compile, build with gcc (no warnings, except unused local variables, which are the program's own) and print exactly tests/pass/NAME.out
#   tests/doc/NAME.ec   `eclang doc` must print exactly tests/doc/NAME.md, with the warnings in tests/doc/NAME.err
#   tests/fail/NAME.ec  must be rejected by the compiler with a message containing its first line's `// error:` text
#   tests/panic/NAME.ec must compile and build, then stop at runtime with a message containing its first line's `// panic:` text
# Usage: tests/run.sh [name-filter]
cd "$(dirname "$0")/.." || exit 1
shopt -s nullglob
JAR=target/eclang-0.1.0.jar
OUT=$(mktemp -d)
trap 'rm -rf "$OUT"' EXIT
passed=0; failed=0

fail() { echo "FAIL $1: $2"; failed=$((failed + 1)); }

# Unit tests of the type system (src/main/kotlin/github/mrh0/ect2/Test.kt).
if [ -z "$1" ]; then
    if java -cp "$JAR" github.mrh0.ect2.TestKt > "$OUT/ect2.out" 2>&1; then echo "pass ect2 type system"; passed=$((passed + 1))
    else fail "ect2" "$(grep FAIL "$OUT/ect2.out")"; fi
fi

# The compiler as a library: several compilations in one JVM, and errors collected from every function.
if [ -z "$1" ] || [[ compiler == *"$1"* ]]; then
    if java -cp "$JAR" github.mrh0.eclang.CompilerTestKt > "$OUT/compiler.out" 2>&1; then echo "pass compiler"; passed=$((passed + 1))
    else fail "compiler" "$(grep FAIL "$OUT/compiler.out")"; fi
fi

# `eclang run` compiles, builds and runs a program in one step.
if [ -z "$1" ] || [[ run == *"$1"* ]]; then
    if java -jar "$JAR" run tests/pass/union-copies.ec > "$OUT/run.out" 2> "$OUT/run.err" && diff -q tests/pass/union-copies.out "$OUT/run.out" > /dev/null; then echo "pass eclang run"; passed=$((passed + 1))
    else fail "eclang run" "$(head -5 "$OUT/run.err" "$OUT/run.out")"; fi
fi

# `eclang run file -- args`: what follows `--` reaches the program.
if [ -z "$1" ] || [[ args == *"$1"* ]]; then
    if [ "$(java -jar "$JAR" run tests/pass/stdlib-args.ec -- one "two words" 2>&1 | tr '\n' '|')" = "2|one|two words|true|" ]; then echo "pass eclang run with arguments"; passed=$((passed + 1))
    else fail "eclang run with arguments" "the program didn't get its arguments"; fi
fi

# The bundled C bindings must match their C headers (see tools/check-bindings.py).
if [ -z "$1" ] || [[ bindings == *"$1"* ]]; then
    bindings=()
    for f in src/main/resources/*.ec src/main/resources/c/*.ec src/main/resources/apr/*.ec; do
        bindings+=("core:${f#src/main/resources/}")
    done
    if tools/check-bindings.py "$OUT/bindings" "${bindings[@]}" > "$OUT/bindings.out" 2>&1; then echo "pass bindings (${#bindings[@]} files)"; passed=$((passed + 1))
    else fail "bindings" "$(grep -m5 -E 'error' "$OUT/bindings.out")"; fi
fi

for src in tests/pass/*${1}*.ec; do
    name=$(basename "$src" .ec)
    if ! java -jar "$JAR" -c "$src" -o "$OUT/$name.c" 2> "$OUT/$name.err"; then fail "$name" "$(cat "$OUT/$name.err")"; continue; fi
    if ! gcc -Wall -Wextra -Werror -Wno-unused-variable -Wno-unused-but-set-variable "$OUT/$name.c" -o "$OUT/$name" -I/usr/include/apr-1.0 -lapr-1 -lm 2> "$OUT/$name.err"; then fail "$name" "gcc: $(head -5 "$OUT/$name.err")"; continue; fi
    if ! "$OUT/$name" > "$OUT/$name.out" 2>&1; then fail "$name" "exited with an error: $(tail -3 "$OUT/$name.out")"; continue; fi
    if ! diff -u "tests/pass/$name.out" "$OUT/$name.out" > "$OUT/$name.diff"; then fail "$name" "wrong output:"; cat "$OUT/$name.diff"; continue; fi
    echo "pass $name"; passed=$((passed + 1))
done

# `eclang doc`: tests/doc/NAME.ec must give the Markdown tests/doc/NAME.md, and the warnings tests/doc/NAME.err (none without it).
for src in tests/doc/*${1}*.ec; do
    name=$(basename "$src" .ec)
    if ! java -jar "$JAR" doc "$src" > "$OUT/$name.md" 2> "$OUT/$name.doc.err"; then fail "doc $name" "$(cat "$OUT/$name.doc.err")"; continue; fi
    if ! diff -u "tests/doc/$name.md" "$OUT/$name.md" > "$OUT/$name.diff"; then fail "doc $name" "wrong Markdown:"; cat "$OUT/$name.diff"; continue; fi
    expected=tests/doc/$name.err; [ -f "$expected" ] || expected=/dev/null
    if ! diff -u "$expected" "$OUT/$name.doc.err" > "$OUT/$name.diff"; then fail "doc $name" "wrong warnings:"; cat "$OUT/$name.diff"; continue; fi
    echo "pass doc $name"; passed=$((passed + 1))
done

for src in tests/panic/*${1}*.ec; do
    name=$(basename "$src" .ec)
    expected=$(head -1 "$src" | sed 's|^// panic: ||')
    if ! java -jar "$JAR" -c "$src" -o "$OUT/$name.c" 2> "$OUT/$name.err"; then fail "$name" "$(cat "$OUT/$name.err")"; continue; fi
    if ! gcc -Wall -Wextra -Werror -Wno-unused-variable -Wno-unused-but-set-variable "$OUT/$name.c" -o "$OUT/$name" -I/usr/include/apr-1.0 -lapr-1 -lm 2> "$OUT/$name.err"; then fail "$name" "gcc: $(head -5 "$OUT/$name.err")"; continue; fi
    # Its stderr redirected, so the shell's "Aborted" report of the expected crash doesn't clutter the output.
    if { "$OUT/$name" > "$OUT/$name.out" 2>&1; } 2> /dev/null; then fail "$name" "ran to the end, but should stop with: $expected"; continue; fi
    if ! grep -qF -- "$expected" "$OUT/$name.out"; then fail "$name" "expected '$expected', got: $(tail -3 "$OUT/$name.out")"; continue; fi
    echo "pass $name (stopped)"; passed=$((passed + 1))
done

for src in tests/fail/*${1}*.ec; do
    name=$(basename "$src" .ec)
    expected=$(head -1 "$src" | sed 's|^// error: ||')
    if java -jar "$JAR" -c "$src" -o "$OUT/$name.c" 2> "$OUT/$name.err" > /dev/null; then fail "$name" "compiled, but should fail with: $expected"; continue; fi
    if ! grep -qF -- "$expected" "$OUT/$name.err"; then fail "$name" "expected '$expected', got: $(cat "$OUT/$name.err")"; continue; fi
    echo "pass $name (rejected)"; passed=$((passed + 1))
done

echo "$passed passed, $failed failed"
[ "$failed" -eq 0 ]
