#!/usr/bin/env node
// Generates the TypeScript parser (src/server/antlr) from the compiler's grammar, src/main/antlr4/Eclang.g4. Run it after
// editing the grammar. Uses the ANTLR 4.13.1 tool from the local Maven cache (run `mvn package` once first), like
// tools/regen-parser.sh does for the compiler's Java parser.
//
// The grammar's @lexer blocks are Java (the indentation tracking and the ecdoc handling), so they are cut out of a
// temporary copy. src/server/lexer.ts does the same job in TypeScript.
import { execFileSync } from "node:child_process";
import { mkdtempSync, readdirSync, readFileSync, rmSync, statSync, writeFileSync } from "node:fs";
import { homedir, tmpdir } from "node:os";
import { dirname, join, resolve } from "node:path";
import { fileURLToPath } from "node:url";

const here = dirname(fileURLToPath(import.meta.url));
const repo = resolve(here, "../../..");
const out = resolve(here, "../src/server/antlr");

/** `source` without the `@lexer::name { ... }` blocks (braces balance in them). */
function withoutLexerActions(source) {
    let result = source;
    for (;;) {
        const start = result.search(/@lexer::\w+\s*\{/);
        if (start < 0) return result;
        let i = result.indexOf("{", start), depth = 0;
        do {
            if (result[i] === "{") depth++;
            else if (result[i] === "}") depth--;
            i++;
        } while (depth > 0);
        result = result.slice(0, start) + result.slice(i);
    }
}

function jars(dir) {
    let found = [];
    let entries;
    try { entries = readdirSync(dir); } catch { return found; }
    for (const entry of entries) {
        const path = join(dir, entry);
        if (statSync(path).isDirectory()) found = found.concat(jars(path));
        else if (entry.endsWith(".jar") && !entry.includes("sources")) found.push(path);
    }
    return found;
}

const m2 = process.env.M2_REPO ?? join(homedir(), ".m2/repository");
const classpath = ["org/antlr/antlr4/4.13.1", "org/antlr/antlr4-runtime/4.13.1", "org/antlr/ST4", "org/antlr/antlr-runtime/3.5.3",
    "org/abego", "com/ibm/icu", "org/glassfish/javax.json"].flatMap(p => jars(join(m2, p)));
if (classpath.length === 0) {
    console.error(`ANTLR 4.13.1 not found in ${m2}; run 'mvn package' in the repository root first.`);
    process.exit(1);
}

const temp = mkdtempSync(join(tmpdir(), "eclang-grammar-"));
try {
    const grammar = join(temp, "Eclang.g4");
    writeFileSync(grammar, withoutLexerActions(readFileSync(join(repo, "src/main/antlr4/Eclang.g4"), "utf8")));
    rmSync(out, { recursive: true, force: true });
    execFileSync("java", ["-cp", classpath.join(":"), "org.antlr.v4.Tool", "-Dlanguage=TypeScript", "-no-listener", "-no-visitor",
        "-o", out, "-Xexact-output-dir", grammar], { stdio: "inherit" });
    for (const f of readdirSync(out)) {
        if (!f.endsWith(".ts")) { rmSync(join(out, f)); continue; }
        // Machine-written: not worth type-checking, and the temporary path of the grammar must not end up in it.
        const path = join(out, f);
        writeFileSync(path, "// @ts-nocheck\n" + readFileSync(path, "utf8").replace(/^\/\/ Generated from .*$/m, "// Generated from src/main/antlr4/Eclang.g4 by scripts/gen-parser.mjs"));
    }
} finally {
    rmSync(temp, { recursive: true, force: true });
}
console.log("Parser generated in src/server/antlr.");
