#!/usr/bin/env bash
# Regenerates the checked-in ANTLR parser (src/main/java/github/mrh0/eclang/antlr, package github.mrh0.eclang.antlr)
# from src/main/antlr4/Eclang.g4. Uses the ANTLR 4.13.1 tool from the local Maven cache (run `mvn package` once first).
set -e
cd "$(dirname "$0")/.."
M2="${M2_REPO:-$HOME/.m2/repository}"
CP=$(find "$M2/org/antlr/antlr4/4.13.1" "$M2/org/antlr/antlr4-runtime/4.13.1" "$M2/org/antlr/ST4" "$M2/org/antlr/antlr-runtime/3.5.3" \
    "$M2/org/abego" "$M2/com/ibm/icu" "$M2/org/glassfish/javax.json" -name '*.jar' ! -name '*sources*' 2>/dev/null | tr '\n' ':')
[ -n "$CP" ] || { echo "ANTLR 4.13.1 not found in $M2; run 'mvn package' first." >&2; exit 1; }
java -cp "$CP" org.antlr.v4.Tool -visitor -package github.mrh0.eclang.antlr \
    -o src/main/java/github/mrh0/eclang/antlr -Xexact-output-dir src/main/antlr4/Eclang.g4
echo "Parser regenerated."
