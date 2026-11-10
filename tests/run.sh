#!/usr/bin/env bash
# Runs the eclang test suite. Build the compiler first: `mvn clean package`.
#   the ect2 unit tests, the compiler tests (src/main/kotlin/github/mrh0/eclang/CompilerTest.kt), `eclang run`, a check that the bundled `declare fn` bindings (core:*.ec, core:c/*.ec, core:apr/*.ec, core:vendor/*.ec) match their C headers, and core:term.ec in a pseudo-terminal (tests/term-pty.py)
#   tests/pass/NAME.ec  must compile, build with gcc (no warnings, except unused local variables, which are the program's own) and print exactly tests/pass/NAME.out,
#                       and with a tests/pass/NAME.release.out, print exactly that when built with `--release` (no `debug` statements)
#   tests/fail-c/NAME.ec must compile, then be rejected by gcc with its first line's `// gcc:` text (a `const assert` only C can check)
#   tests/doc/NAME.ec   `eclang doc` must print exactly tests/doc/NAME.md, with the warnings in tests/doc/NAME.err
#   tests/fail/NAME.ec  must be rejected by the compiler with a message containing its first line's `// error:` text
#   tests/lib/NAME.ec   `eclang lib` must build it, and the C program tests/lib/NAME.main.c linked to it must print exactly tests/lib/NAME.out
#   tests/link/greet.ec must build and run against a C library given with `use "lib:greet"`
#   tests/panic/NAME.ec must compile and build, then stop at runtime with a message containing its first line's `// panic:` text
#   tests/memory/freed.ec built with `run --check-memory` (AddressSanitizer) must be reported for using freed memory
#   bindings/raylib     checked against raylib.h when it is installed or on C_INCLUDE_PATH, skipped otherwise
#   bindings/ffmpeg     checked against FFmpeg's headers, and its examples run, when they are installed or on C_INCLUDE_PATH, skipped otherwise
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

# `eclang run --check-memory`: a malloc per allocation, under AddressSanitizer, which must report a use of memory a freed pool
# held (tests/memory/freed.ec, line 13), and a program that uses memory correctly runs as usual.
if [ -z "$1" ] || [[ check-memory == *"$1"* ]]; then
    if echo 'int main(void) { return 0; }' | gcc -fsanitize=address -x c - -o "$OUT/asan-probe" > /dev/null 2>&1; then
        java -jar "$JAR" run -m tests/memory/freed.ec > "$OUT/memory.out" 2> "$OUT/memory.err"
        if grep -q 'heap-use-after-free' "$OUT/memory.err" && grep -q 'freed.ec:13' "$OUT/memory.err"; then echo "pass eclang run --check-memory finds a use of freed memory"; passed=$((passed + 1))
        else fail "check-memory" "$(head -5 "$OUT/memory.err")"; fi
        if java -jar "$JAR" run -m tests/pass/round-pools.ec > "$OUT/memory-ok.out" 2> "$OUT/memory-ok.err" && diff -q tests/pass/round-pools.out "$OUT/memory-ok.out" > /dev/null; then echo "pass eclang run --check-memory of a correct program"; passed=$((passed + 1))
        else fail "check-memory ok" "$(head -5 "$OUT/memory-ok.err")"; fi
    else echo "skip eclang run --check-memory (gcc has no AddressSanitizer here)"; fi
fi

# `use "lib:greet"` links a C library: `run` and `lib` pass -lgreet, once, and the C file says so.
if [ -z "$1" ] || [[ link == *"$1"* ]]; then
    gcc -fPIC -c tests/link/greet.c -o "$OUT/greet.o" && ar rcs "$OUT/libgreet.a" "$OUT/greet.o"
    export_paths=(env "LIBRARY_PATH=$OUT" "C_INCLUDE_PATH=tests/link")
    if "${export_paths[@]}" java -jar "$JAR" run tests/link/greet.ec > "$OUT/link.out" 2> "$OUT/link.err" && diff -q tests/link/greet.out "$OUT/link.out" > /dev/null; then echo "pass eclang run with a C library"; passed=$((passed + 1))
    else fail "link run" "$(head -5 "$OUT/link.err" "$OUT/link.out")"; fi
    if java -jar "$JAR" -c tests/link/greet.ec -o "$OUT/link.c" > /dev/null 2>&1 && grep -q 'Link with -lgreet' "$OUT/link.c"; then echo "pass C file names its libraries"; passed=$((passed + 1))
    else fail "link comment" "the C file doesn't say to link with -lgreet"; fi
    if "${export_paths[@]}" java -jar "$JAR" lib tests/link/greetlib.ec -o "$OUT/greetlib" > /dev/null 2> "$OUT/link.err" && ! ldd -r "$OUT/greetlib/libgreetlib.so" 2>&1 | grep -q 'undefined symbol: greet_add'; then echo "pass eclang lib with a C library"; passed=$((passed + 1))
    else fail "link lib" "$(head -5 "$OUT/link.err")"; fi
fi

# The bundled C bindings must match their C headers (see tools/check-bindings.py).
if [ -z "$1" ] || [[ bindings == *"$1"* ]]; then
    bindings=()
    for f in src/main/resources/*.ec src/main/resources/c/*.ec src/main/resources/apr/*.ec src/main/resources/vendor/*.ec; do
        bindings+=("core:${f#src/main/resources/}")
    done
    if tools/check-bindings.py "$OUT/bindings" "${bindings[@]}" > "$OUT/bindings.out" 2>&1; then echo "pass bindings (${#bindings[@]} files)"; passed=$((passed + 1))
    else fail "bindings" "$(grep -m5 -E 'error' "$OUT/bindings.out")"; fi
fi

# bindings/raylib (not part of core:), when raylib.h of the same version is installed or on C_INCLUDE_PATH: raylib.ec
# must be what its generator makes of the header, its `declare fn`s must match it, and its examples must build.
if [ -z "$1" ] || [[ raylib == *"$1"* ]]; then
    header=$(echo '#include <raylib.h>' | gcc -E -H -x c - 2>&1 > /dev/null | grep -m1 '/raylib\.h$' | sed 's/^\.* //')
    version=$(grep -m1 -oP '#define RAYLIB_VERSION\s+"\K[^"]+' "${header:-/dev/null}" 2>/dev/null)
    if [ -z "$header" ]; then echo "skip raylib bindings (raylib.h not found: put its directory on C_INCLUDE_PATH)"
    elif ! grep -q "Bindings for raylib $version " bindings/raylib/raylib.ec; then echo "skip raylib bindings ($header is raylib $version, the bindings are for another version)"
    else
        problem=""
        bindings/raylib/generate.py "$header" | cmp -s - bindings/raylib/raylib.ec || problem="raylib.ec isn't what bindings/raylib/generate.py makes of $header: run it again"
        if [ -z "$problem" ] && ! tools/check-bindings.py "$OUT/raylib" bindings/raylib/raylib.ec > "$OUT/raylib.out" 2>&1; then problem="$(grep -m5 error "$OUT/raylib.out")"; fi
        for example in bindings/raylib/examples/*.ec; do
            [ -n "$problem" ] && break
            name=$(basename "$example" .ec)
            if ! java -jar "$JAR" -c "$example" -o "$OUT/raylib-$name.c" > "$OUT/raylib-$name.err" 2>&1 || [ -s "$OUT/raylib-$name.err" ]; then problem="$example: $(head -5 "$OUT/raylib-$name.err")"
            elif ! gcc -fwrapv -fno-strict-aliasing -Wall -Wextra -Werror -c "$OUT/raylib-$name.c" -o "$OUT/raylib-$name.o" -I/usr/include/apr-1.0 2> "$OUT/raylib-$name.err"; then problem="$example: gcc: $(head -5 "$OUT/raylib-$name.err")"; fi
        done
        if [ -z "$problem" ]; then echo "pass raylib bindings (raylib $version)"; passed=$((passed + 1))
        else fail "raylib" "$problem"; fi
    fi
fi

# bindings/ffmpeg (not part of core:), when FFmpeg's headers are installed or on C_INCLUDE_PATH, of the major versions
# the bindings are for: the `declare fn`s must match them, constants.ec must be what generate.py makes of them (for the
# exact version it was made from), and the examples must build and work: encode.ec writes a video, decode.ec decodes
# it, tone.ec writes audio, and probe.ec reads both. Linking needs the libraries' development files (libav*.so).
if [ -z "$1" ] || [[ ffmpeg == *"$1"* ]]; then
    header=$(echo '#include <libavformat/avformat.h>' | gcc -E -H -x c - 2>&1 > /dev/null | grep -m1 '/libavformat/avformat\.h$' | sed 's/^\.* //')
    if [ -z "$header" ]; then echo "skip ffmpeg bindings (libavformat/avformat.h not found: install FFmpeg's development files or put their directory on C_INCLUDE_PATH)"
    else
        include=$(dirname "$(dirname "$header")")
        problem=""
        bindings/ffmpeg/generate.py "$include" > "$OUT/ffmpeg-constants.ec" 2> "$OUT/ffmpeg-generate.err" || problem="generate.py failed: $(head -3 "$OUT/ffmpeg-generate.err")"
        # Line 2 of constants.ec names the versions, "libavutil 60.8.100, ...": the majors must match, the rest may differ.
        made=$(sed -n 2p "$OUT/ffmpeg-constants.ec"); ours=$(sed -n 2p bindings/ffmpeg/constants.ec)
        majors() { sed -E 's/([0-9]+)\.[0-9]+\.[0-9]+/\1/g'; }
        if [ -z "$problem" ] && [ "$(majors <<< "$made")" != "$(majors <<< "$ours")" ]; then echo "skip ffmpeg bindings (the headers in $include are${made#*:} The bindings are for${ours#*:})"
        else
            if [ -z "$problem" ] && [ "$made" == "$ours" ] && ! cmp -s "$OUT/ffmpeg-constants.ec" bindings/ffmpeg/constants.ec; then problem="constants.ec isn't what bindings/ffmpeg/generate.py makes of $include: run it again"; fi
            if [ -z "$problem" ] && ! tools/check-bindings.py "$OUT/ffmpeg" bindings/ffmpeg/{avutil,avcodec,avformat,swscale,swresample}.ec > "$OUT/ffmpeg.out" 2>&1; then problem="$(grep -m5 error "$OUT/ffmpeg.out")"; fi
            for example in bindings/ffmpeg/examples/*.ec; do
                [ -n "$problem" ] && break
                name=$(basename "$example" .ec)
                if ! java -jar "$JAR" -c "$example" -o "$OUT/ffmpeg-$name.c" > "$OUT/ffmpeg-$name.err" 2>&1 || [ -s "$OUT/ffmpeg-$name.err" ]; then problem="$example: $(head -5 "$OUT/ffmpeg-$name.err")"
                elif ! gcc -fwrapv -fno-strict-aliasing -Wall -Wextra -Werror -Wno-unused-variable -Wno-unused-but-set-variable "$OUT/ffmpeg-$name.c" -o "$OUT/ffmpeg-$name" -I/usr/include/apr-1.0 -lavformat -lavcodec -lswscale -lswresample -lavutil -lapr-1 -lm 2> "$OUT/ffmpeg-$name.err"; then problem="$example: gcc: $(head -5 "$OUT/ffmpeg-$name.err")"; fi
            done
            # What each run must print (a part of its output), with the arguments it is run with.
            expect() {
                local name=$1 wanted=$2; shift 2
                [ -n "$problem" ] && return
                if ! "$OUT/ffmpeg-$name" "$@" > "$OUT/ffmpeg-$name.run" 2>&1; then problem="examples/$name.ec failed: $(tail -3 "$OUT/ffmpeg-$name.run")"
                elif ! grep -qF "$wanted" "$OUT/ffmpeg-$name.run"; then problem="examples/$name.ec should print '$wanted', but printed: $(head -5 "$OUT/ffmpeg-$name.run")"; fi
            }
            expect encode "wrote 75 frames of 320x240 yuv420p (ffv1)" "$OUT/ffmpeg.mkv" ffv1
            expect decode "decoded 75 frames (7 key frames) of 320x240 yuv420p (ffv1)" "$OUT/ffmpeg.mkv" "$OUT/ffmpeg.ppm"
            expect tone "wrote 88200 samples of 44100 Hz stereo s16 (pcm_s16le)" "$OUT/ffmpeg.wav"
            expect probe "stream 0: video: ffv1, 320x240, yuv420p, 25.00 fps" "$OUT/ffmpeg.mkv"
            expect probe "stream 0: audio: pcm_s16le, 44100 Hz, 2 channels, s16" "$OUT/ffmpeg.wav"   # a plain WAV has no channel mask
            if [ -z "$problem" ]; then echo "pass ffmpeg bindings ($(sed -E 's/^.*: (.*)\.$/\1/' <<< "$made"))"; passed=$((passed + 1))
            else fail "ffmpeg" "$problem"; fi
        fi
    fi
fi

# core:term.ec in a real pseudo-terminal: raw mode, keys, and that the terminal comes back however the program ends.
if [ -z "$1" ] || [[ term-pty == *"$1"* ]]; then
    if tests/term-pty.py "$JAR" "$OUT/pty" > "$OUT/pty.out" 2>&1; then echo "pass term in a pseudo-terminal ($(grep -c '^ok' "$OUT/pty.out") scenarios)"; passed=$((passed + 1))
    else fail "term-pty" "$(grep -v '^ok' "$OUT/pty.out" | head -5)"; fi
fi

for src in tests/pass/*${1}*.ec; do
    name=$(basename "$src" .ec)
    if ! java -jar "$JAR" -c "$src" -o "$OUT/$name.c" 2> "$OUT/$name.err"; then fail "$name" "$(cat "$OUT/$name.err")"; continue; fi
    if ! gcc -fwrapv -fno-strict-aliasing -Wall -Wextra -Werror -Wno-unused-variable -Wno-unused-but-set-variable "$OUT/$name.c" -o "$OUT/$name" -I/usr/include/apr-1.0 -lapr-1 -lm 2> "$OUT/$name.err"; then fail "$name" "gcc: $(head -5 "$OUT/$name.err")"; continue; fi
    if ! "$OUT/$name" > "$OUT/$name.out" 2>&1; then fail "$name" "exited with an error: $(tail -3 "$OUT/$name.out")"; continue; fi
    if ! diff -u "tests/pass/$name.out" "$OUT/$name.out" > "$OUT/$name.diff"; then fail "$name" "wrong output:"; cat "$OUT/$name.diff"; continue; fi
    echo "pass $name"; passed=$((passed + 1))
done

# `--release` leaves out `debug` statements and `debug requires`/`ensures`.
for expected in tests/pass/*${1}*.release.out; do
    name=$(basename "$expected" .release.out)
    if ! java -jar "$JAR" -c "tests/pass/$name.ec" -r -o "$OUT/$name.release.c" 2> "$OUT/$name.err"; then fail "$name --release" "$(cat "$OUT/$name.err")"; continue; fi
    if ! gcc -fwrapv -fno-strict-aliasing -Wall -Wextra -Werror -Wno-unused-variable -Wno-unused-but-set-variable "$OUT/$name.release.c" -o "$OUT/$name.release" -I/usr/include/apr-1.0 -lapr-1 -lm 2> "$OUT/$name.err"; then fail "$name --release" "gcc: $(head -5 "$OUT/$name.err")"; continue; fi
    if ! "$OUT/$name.release" > "$OUT/$name.release.out" 2>&1; then fail "$name --release" "exited with an error: $(tail -3 "$OUT/$name.release.out")"; continue; fi
    if ! diff -u "$expected" "$OUT/$name.release.out" > "$OUT/$name.diff"; then fail "$name --release" "wrong output:"; cat "$OUT/$name.diff"; continue; fi
    echo "pass $name --release"; passed=$((passed + 1))
done

# A `const assert` that needs `sizeof` becomes a C `_Static_assert`, so the C compiler rejects it.
for src in tests/fail-c/*${1}*.ec; do
    name=$(basename "$src" .ec)
    expected=$(head -1 "$src" | sed 's|^// gcc: ||')
    if ! java -jar "$JAR" -c "$src" -o "$OUT/$name.c" 2> "$OUT/$name.err"; then fail "$name" "$(cat "$OUT/$name.err")"; continue; fi
    if gcc -fwrapv -fno-strict-aliasing "$OUT/$name.c" -o "$OUT/$name" -I/usr/include/apr-1.0 -lapr-1 -lm 2> "$OUT/$name.err"; then fail "$name" "gcc built it, but should fail with: $expected"; continue; fi
    if ! grep -qF -- "$expected" "$OUT/$name.err"; then fail "$name" "expected '$expected', got: $(grep -m3 error "$OUT/$name.err")"; continue; fi
    echo "pass $name (rejected by C)"; passed=$((passed + 1))
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

# `eclang lib`: tests/lib/NAME.ec must build as a shared and as a static library, and the C program tests/lib/NAME.main.c must print tests/lib/NAME.out with each.
for src in tests/lib/*${1}*.ec; do
    name=$(basename "$src" .ec)
    dir=$OUT/lib-$name; c=tests/lib/$name.main.c
    flags=(-Wall -Wextra -Werror -I"$dir") # no APR: the header only needs it for an API with APR's types
    if ! java -jar "$JAR" lib "$src" -o "$dir" 2> "$OUT/$name.err"; then fail "lib $name" "$(cat "$OUT/$name.err")"; continue; fi
    if ! gcc "${flags[@]}" "$c" -o "$dir/shared" -L"$dir" -l"$name" -Wl,-rpath,"$dir" -lpthread 2> "$OUT/$name.err"; then fail "lib $name" "gcc: $(head -5 "$OUT/$name.err")"; continue; fi
    if ! java -jar "$JAR" lib "$src" -s -o "$dir" 2> "$OUT/$name.err"; then fail "lib $name" "$(cat "$OUT/$name.err")"; continue; fi
    if ! gcc "${flags[@]}" "$c" "$dir/lib$name.a" -o "$dir/static" -lapr-1 -lm -lpthread 2> "$OUT/$name.err"; then fail "lib $name" "gcc: $(head -5 "$OUT/$name.err")"; continue; fi
    wrong=
    for program in shared static; do
        "$dir/$program" > "$OUT/$name.out" 2>&1 && diff -u "tests/lib/$name.out" "$OUT/$name.out" > "$OUT/$name.diff" || wrong=$program
    done
    if [ -n "$wrong" ]; then fail "lib $name" "wrong output with the $wrong library:"; cat "$OUT/$name.diff" "$OUT/$name.out"; continue; fi
    # Only what is exported is visible to the linker.
    if nm -D --defined-only "$dir/lib$name.so" | grep -qE ' T (hidden|geo__)'; then fail "lib $name" "exports a function that isn't \`export\`ed"; continue; fi
    echo "pass lib $name"; passed=$((passed + 1))
done

for src in tests/panic/*${1}*.ec; do
    name=$(basename "$src" .ec)
    expected=$(head -1 "$src" | sed 's|^// panic: ||')
    if ! java -jar "$JAR" -c "$src" -o "$OUT/$name.c" 2> "$OUT/$name.err"; then fail "$name" "$(cat "$OUT/$name.err")"; continue; fi
    if ! gcc -fwrapv -fno-strict-aliasing -Wall -Wextra -Werror -Wno-unused-variable -Wno-unused-but-set-variable "$OUT/$name.c" -o "$OUT/$name" -I/usr/include/apr-1.0 -lapr-1 -lm 2> "$OUT/$name.err"; then fail "$name" "gcc: $(head -5 "$OUT/$name.err")"; continue; fi
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
