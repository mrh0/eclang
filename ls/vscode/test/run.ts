// The tests of the extension. `npm test` runs them; the compiler jar must be built (`mvn package` in the repository root).
//
// The server's parsing is a port of the compiler's, so most of this compares the two: the compiler (through
// test/oracle/Oracle.java) and the port read every .ec file of the repository and damaged copies of them, and must agree
// on the tokens, on the syntax error and on the ecdocs.
import assert from "node:assert/strict";
import { execFileSync, spawn } from "node:child_process";
import { mkdirSync, mkdtempSync, readdirSync, readFileSync, rmSync, statSync, writeFileSync } from "node:fs";
import { tmpdir } from "node:os";
import { join, resolve } from "node:path";
import { pathToFileURL } from "node:url";
import { CharStream, Token } from "antlr4";
import * as oniguruma from "vscode-oniguruma";
import { INITIAL, Registry } from "vscode-textmate";
import { Checker } from "../src/server/checker";
import { complete, placeAt } from "../src/server/completion";
import { rangeAt } from "../src/server/diagnostics";
import { definition, hover } from "../src/server/hover";
import { lines } from "../src/server/kotlin";
import { IndentingLexer } from "../src/server/lexer";
import { parse } from "../src/server/parse";
import { Project } from "../src/server/project";

const root = resolve(__dirname, "../..");
const repo = resolve(root, "../..");
const jar = join(repo, "target/eclang-0.1.0.jar");
let failures = 0;

function report(name: string, problems: string[], detail = ""): void {
    if (problems.length === 0) { console.log(`pass ${name}${detail ? ` (${detail})` : ""}`); return; }
    failures++;
    console.log(`FAIL ${name}: ${problems.length} problem(s)`);
    problems.slice(0, 5).forEach(p => console.log("  " + p.slice(0, 1500)));
}

function ecFiles(dir: string): string[] {
    return readdirSync(dir).flatMap(entry => {
        const path = join(dir, entry);
        return statSync(path).isDirectory() ? ecFiles(path) : entry.endsWith(".ec") ? [path] : [];
    });
}

/** A small seeded generator, so a failure can be reproduced. */
function random(seed: number) {
    return (n: number) => {
        seed = (seed * 1103515245 + 12345) & 0x7fffffff;
        return Math.floor((seed / 0x7fffffff) * n);
    };
}

/** Copies of [text] with something broken, the way code is while it's being typed. */
function damaged(text: string, seed: number): string[] {
    const next = random(seed);
    const all = lines(text);
    const line = () => next(all.length);
    const edit = (f: (l: string) => string) => { const copy = [...all], i = line(); copy[i] = f(copy[i]); return copy.join("\n"); };
    const cut = (l: string) => { const i = next(l.length + 1); return l.slice(0, i) + l.slice(i + 1 + next(4)); };
    const junk = ["(", ")", "{", "}", "[", "]", "\"", "'", ";", " do", " as", ":", "=", "?", "++", "->", "\t", "  ", "return ", "@", "#", "/**", "é"];
    const insert = (l: string) => { const i = next(l.length + 1); return l.slice(0, i) + junk[next(junk.length)] + l.slice(i); };
    return [
        edit(cut), edit(cut), edit(insert), edit(insert), edit(insert),
        edit(l => l.slice(0, next(l.length + 1))),
        edit(l => "  " + l),
        edit(l => l.replace(/^ {1,4}/, "")),
        all.filter((_, i) => i !== line()).join("\n"),
        text.slice(0, next(text.length + 1)),
    ];
}

function tokensOf(text: string): unknown[] {
    const lexer = new IndentingLexer(new CharStream(text));
    lexer.removeErrorListeners();
    const tokens: unknown[] = [];
    for (let t = lexer.nextToken(); ; t = lexer.nextToken()) {
        tokens.push([t.type, t.line, t.column, t.text]);
        if (t.type === Token.EOF) return tokens;
    }
}

const plain = (x: unknown) => JSON.parse(JSON.stringify(x));
const sortProblems = (docs: any) => {
    docs.problems.sort((a: any, b: any) => JSON.stringify(a).localeCompare(JSON.stringify(b)));
    return docs;
};

function compareWithCompiler(): void {
    const temp = mkdtempSync(join(tmpdir(), "eclang-oracle-"));
    try {
        const corpus = ["tests", "examples", "src/main/resources"].flatMap(d => ecFiles(join(repo, d)));
        const files = [...corpus];
        corpus.forEach((file, i) => damaged(readFileSync(file, "utf8"), i + 1).forEach((text, j) => {
            const path = join(temp, `${i}-${j}.ec`);
            writeFileSync(path, text);
            files.push(path);
        }));
        const results: any[] = execFileSync("java", ["-Xss16m", "-cp", jar, join(root, "test/oracle/Oracle.java")],
            { input: files.join("\n") + "\n", maxBuffer: 1 << 30, encoding: "utf8", stdio: ["pipe", "pipe", "ignore"] })
            .trim().split("\n").map(l => JSON.parse(l));

        const problems: string[] = [];
        let errors = 0, documented = 0;
        for (const expected of results) {
            const text = readFileSync(expected.file, "utf8");
            const name = expected.file.replace(repo + "/", "");
            try {
                assert.deepEqual(plain(tokensOf(text)), expected.tokens);
                const parsed = parse(text);
                if (expected.error) {
                    errors++;
                    assert.deepEqual(plain(parsed.error), expected.error);
                } else {
                    documented++;
                    assert.equal(parsed.error, null);
                    const { scopes, uses, moduleLocation, ...docs } = plain(parsed.docs);
                    assert.deepEqual(sortProblems(docs), sortProblems(expected.docs));
                }
            } catch (e: any) {
                problems.push(`${name}: ${e.message}`);
            }
        }
        report("same as the compiler", problems, `${results.length} files: ${documented} parse, ${errors} syntax errors`);
    } finally {
        rmSync(temp, { recursive: true, force: true });
    }
}

const fixture = `/** Geometry. */
module "geo"
use "core:io.ec"

/** A point. */
rec Point as
    /** Distance from the y axis. */
    x: Double
    y: Double = 0.0

/**
 * The size of {@link Point p}.
 * @param p what to measure
 * @return the size
 * @see Point#x
 */
fn size(p: Point): Double = p.x

fn size(s: String): Double = 1.0

/** Hidden. */
priv fn secret(): Int = 1

fn main(): Int do
    val size2 = size(Point{x = 1.0, y = 2.0})
    val secret = 2
    io.log("\${size2} and size")
    // size
    ret secret
`;

function testHover(): void {
    const problems: string[] = [];
    const file = "/nowhere/fixture.ec";
    const project = new Project(join(root, "dist/core"), () => [], f => f === file ? fixture : undefined);
    const at = (needle: string, offset = 0, from = 0) => {
        const index = fixture.indexOf(needle, from) + offset;
        const before = fixture.slice(0, index).split("\n");
        return hover(project, file, fixture, before.length - 1, before[before.length - 1].length);
    };
    const expect = (what: string, value: string | null, ...parts: string[]) => {
        for (const part of parts) if (!value?.includes(part)) problems.push(`${what}: no '${part}' in ${JSON.stringify(value)}`);
    };
    const nothing = (what: string, value: string | null) => { if (value !== null) problems.push(`${what}: expected nothing, got ${value}`); };

    const call = fixture.indexOf("val size2");
    expect("overloads", at("size(Point", 0, call), "fn size(p: Point): Double", "fn size(s: String): Double", "The size of p.",
        "**Parameters**", "`p` — what to measure", "**Returns** the size", "**See also** `Point#x`");
    expect("declaration", at("size(s: String"), "fn size(s: String): Double");
    expect("type", at("Point as"), "rec Point", "A point.", "`x: Double` — Distance from the y axis.", "In module `geo`");
    expect("field", at("x: Double"), "x: Double", "Distance from the y axis.", "Member of `Point`");
    expect("field through a value", at("p.x", 2), "x: Double", "Member of `Point`");
    expect("field in a literal", at("x = 1.0"), "x: Double");
    expect("parameter", at("p.x"), "p: Point", "what to measure");
    expect("module", at("io.log"), 'module "io"');
    expect("function of a module", at("log("), "fn log(", "In module `io`");
    expect("private in its module", at("secret()"), "Hidden.", "Private to its module");
    nothing("a local", at("size2}"));
    nothing("a local that hides a function", at("secret\n"));
    nothing("a keyword", at("ret secret"));
    nothing("a string", at("and size", 4));
    nothing("a comment", at("// size", 3));
    nothing("an ecdoc", at("Hidden"));
    report("hover", problems);
}

function testDefinition(): void {
    const problems: string[] = [];
    const file = "/nowhere/definitions.ec";
    const source = `use "core:io.ec"
use "core:thread.ec"
use "stdio.h"

/** A point. */
rec Point as
    x: Double

fn size(p: Point): Double = p.x

fn size(s: String): Double = 1.0

fn main(): Int do
    val a = 1
    if a > 0 do
        val b = 2
        io.log("\${b}")
    val b = size(Point{x = 1.0})
    for i in 0..<3 do
        io.log(i)
    ret a + b
`;
    const project = new Project(join(root, "dist/core"), () => [], f => f === file ? source : undefined);
    /** Where the definition of the [nth] `needle` (at [offset] in it) goes, as `file:line:column+length`. */
    const at = (needle: string, nth = 1, offset = 0) => {
        let index = -1;
        for (let i = 0; i < nth; i++) index = source.indexOf(needle, index + 1);
        const before = source.slice(0, index + offset).split("\n");
        return definition(project, file, source, before.length - 1, before[before.length - 1].length)
            .map(d => `${d.file.replace(file, "this").replace(join(root, "dist/core"), "core")}:${d.location.line}:${d.location.column}+${d.length}`);
    };
    const check = (what: string, got: string[], ...want: (string | RegExp)[]) => {
        const ok = got.length === want.length && want.every((w, i) => typeof w === "string" ? got[i] === w : w.test(got[i]));
        if (!ok) problems.push(`${what}: got ${JSON.stringify(got)}, wanted ${want}`);
    };

    check("record", at("Point{"), "this:6:4+5");
    check("type of a parameter", at("Point)"), "this:6:4+5");
    check("overloads", at("size(Point"), "this:9:3+4", "this:11:3+4");
    check("field in a literal", at("x = 1.0"), "this:7:4+1");
    check("field through a value", at("p.x", 1, 2), "this:7:4+1");
    check("parameter", at("p.x"), "this:9:8+1");
    check("local", at("a + b"), "this:14:8+1");
    check("loop variable", at("log(i", 1, 4), "this:19:8+1");
    check("a reused name, inner block", at("${b}", 1, 2), "this:16:12+1");
    check("a reused name, later", at("+ b", 1, 2), "this:18:8+1");
    check("module", at("io.log"), /^core:io\.ec:\d+:7\+4$/);
    check("function of a core module", at("log(").slice(0, 1), /^core:io\.ec:\d+:\d+\+3$/);
    check("use", at("core:io"), "core:io.ec:1:0+0");
    check("use, another file", at("core:thread"), "core:thread.ec:1:0+0");
    check("a C header", at("stdio"));
    check("a keyword", at("ret"));
    check("a comment", at("A point"));
    report("definition", problems);
}

function testRanges(): void {
    const source = ["fn main(): Int do", "    val x = (1", ""];
    const problems: string[] = [];
    const range = (line: number, column: number) => { const r = rangeAt(source, { line, column }); return [r.start.line, r.start.character, r.end.character]; };
    const check = (got: number[], want: number[]) => { if (got.join() !== want.join()) problems.push(`${got} is not ${want}`); };
    check(range(1, 3), [0, 3, 7]); // a word
    check(range(1, 5), [0, 3, 7]); // the middle of a word
    check(range(2, 0), [1, 4, 18 - 4]); // a whole line, as ecdoc tags report
    check(range(2, 14), [1, 13, 14]); // past the end
    check(range(1, 7), [0, 7, 8]); // a bracket
    report("diagnostic ranges", problems);
}

/** A keyword of the grammar that the highlighter doesn't know would go unnoticed. */
async function testHighlighting(): Promise<void> {
    const wasm = readFileSync(join(root, "node_modules/vscode-oniguruma/release/onig.wasm"));
    await oniguruma.loadWASM(wasm.buffer.slice(wasm.byteOffset, wasm.byteOffset + wasm.byteLength) as ArrayBuffer);
    const registry = new Registry({
        onigLib: Promise.resolve({
            createOnigScanner: (patterns: string[]) => new oniguruma.OnigScanner(patterns),
            createOnigString: (s: string) => new oniguruma.OnigString(s),
        }),
        loadGrammar: async () => JSON.parse(readFileSync(join(root, "syntaxes/eclang.tmLanguage.json"), "utf8")),
    });
    const grammar = (await registry.loadGrammar("source.eclang"))!;
    const scopes = (text: string) => {
        let stack = INITIAL;
        const found: { text: string; scopes: string[] }[] = [];
        for (const line of text.split("\n")) {
            const result = grammar.tokenizeLine(line, stack);
            stack = result.ruleStack;
            for (const t of result.tokens) found.push({ text: line.slice(t.startIndex, t.endIndex), scopes: t.scopes });
        }
        return found;
    };
    const problems: string[] = [];
    const expect = (source: string, text: string, scope: string) => {
        const token = scopes(source).find(t => t.text === text);
        if (!token?.scopes.some(s => s.startsWith(scope))) problems.push(`'${text}' in '${source}' is ${token?.scopes.join(" ") ?? "not a token"}, not ${scope}`);
    };

    const words = [...readFileSync(join(repo, "src/main/antlr4/Eclang.g4"), "utf8").replace(/\/\/.*$/gm, "").matchAll(/'([a-z]+)'/g)].map(m => m[1]);
    const lexerOnly = new Set(["n", "r", "t", "c", "e", "x"]); // escapes and `0x`
    for (const word of new Set(words)) if (!lexerOnly.has(word) && word.length > 1) {
        const token = scopes(`${word} `).find(t => t.text === word);
        if (!token?.scopes.some(s => /^(keyword|storage|constant\.language|variable\.language)/.test(s))) problems.push(`keyword '${word}' isn't highlighted`);
    }

    expect("fn fib(n: Int): Int do", "fib", "entity.name.function");
    expect("rec Point as", "Point", "entity.name.type");
    expect("rec Dot extends Point", "Point", "entity.other.inherited-class");
    expect("val a: Int = 1", "Int", "support.type.primitive");
    expect("val a: Thing = 1", "Thing", "entity.name.type");
    expect("val a = MAX_SIZE", "MAX_SIZE", "constant.other");
    expect("x = foo(1)", "foo", "entity.name.function.call");
    expect("x = :red", ":red", "constant.other.symbol");
    expect("x = 0xFF", "0xFF", "constant.numeric.hex");
    expect("x = 1_000", "1_000", "constant.numeric.integer");
    expect("x = 1.5e3", "1.5e3", "constant.numeric.float");
    expect("x = 'a'", "'a'", "string.quoted.single");
    expect("x = \"a\\n\"", "\\n", "constant.character.escape");
    expect("x = \"a ${b + 1} c\"", "${", "punctuation.definition.interpolation");
    expect("x = \"a ${foo(1)} c\"", "foo", "entity.name.function.call");
    expect("x = \"abc\"c", "\"c", "punctuation.definition.string.end");
    expect("pool do", "pool", "keyword.control.pool");
    expect("pool p do", "pool", "keyword.control.pool");
    expect("for i in 0..<10 do", "..<", "keyword.operator.range");
    expect("x |> y", "|>", "keyword.operator.pipe");
    expect("// fn foo", "// fn foo", "comment.line");
    expect("/* fn */ val", "val", "storage.type");
    expect("/** @param x the x */", "@param", "storage.type.class.ecdoc");
    expect("/** @param x the x */", "x", "variable.parameter.ecdoc");
    expect("/** {@link Point} */", "Point", "markup.inline.raw");
    expect("x = \"open\nval y = 1", "val", "storage.type"); // an unclosed string ends with its line

    report("highlighting", problems);
}

/** Runs the bundled server over stdio, as an editor does. */
async function testServer(): Promise<void> {
    const dir = mkdtempSync(join(tmpdir(), "eclang-server-"));
    const server = spawn("node", [join(root, "dist/server.js"), "--stdio"], { stdio: ["pipe", "pipe", "inherit"] });
    const problems: string[] = [];
    try {
        const lib = join(dir, "lib.ec");
        writeFileSync(lib, 'module "lib"\n\n/** Doubles it. */\nfn twice(x: Int): Int = x * 2\n');
        const file = join(dir, "main.ec");
        const uri = pathToFileURL(file).toString();

        let buffer = Buffer.alloc(0);
        const waiting = new Map<number, (r: any) => void>();
        const notes: any[] = [];
        let onNote = () => {};
        server.stdout.on("data", chunk => {
            buffer = Buffer.concat([buffer, chunk]);
            for (;;) {
                const end = buffer.indexOf("\r\n\r\n");
                if (end < 0) return;
                const length = Number(/Content-Length: (\d+)/.exec(buffer.toString("ascii", 0, end))![1]);
                if (buffer.length < end + 4 + length) return;
                const message = JSON.parse(buffer.toString("utf8", end + 4, end + 4 + length));
                buffer = buffer.subarray(end + 4 + length);
                if (message.id !== undefined && waiting.has(message.id)) waiting.get(message.id)!(message.result);
                else { notes.push(message); onNote(); }
            }
        });
        const send = (message: object) => {
            const body = JSON.stringify({ jsonrpc: "2.0", ...message });
            server.stdin.write(`Content-Length: ${Buffer.byteLength(body)}\r\n\r\n${body}`);
        };
        let id = 0;
        const request = (method: string, params: object) => new Promise<any>(done => { waiting.set(++id, done); send({ id, method, params }); });
        const diagnostics = () => new Promise<any[]>(done => {
            notes.length = 0;
            onNote = () => {
                const note = notes.find(n => n.method === "textDocument/publishDiagnostics");
                if (note) done(note.params.diagnostics);
            };
        });

        const init = await request("initialize", { capabilities: {}, processId: process.pid, rootUri: null, workspaceFolders: null, initializationOptions: { compilerJar: jar } });
        if (init.capabilities.hoverProvider !== true) problems.push("no hover capability");
        send({ method: "initialized", params: {} });

        const good = 'use "lib.ec"\n\nfn main(): Int do\n    ret lib.twice(2)\n';
        let published = diagnostics();
        send({ method: "textDocument/didOpen", params: { textDocument: { uri, languageId: "eclang", version: 1, text: good } } });
        let found = await published;
        if (found.length !== 0) problems.push(`a good file has diagnostics: ${JSON.stringify(found)}`);

        const shown = await request("textDocument/hover", { textDocument: { uri }, position: { line: 3, character: 13 } });
        if (!shown?.contents?.value?.includes("Doubles it.")) problems.push(`hover across files: ${JSON.stringify(shown)}`);

        const offered = await request("textDocument/completion", { textDocument: { uri }, position: { line: 3, character: 12 } });
        if (!offered?.items?.some((i: any) => i.label === "twice")) problems.push(`completion across files: ${JSON.stringify(offered).slice(0, 300)}`);
        const twice = offered?.items?.find((i: any) => i.label === "twice");
        const resolved = twice && await request("completionItem/resolve", twice);
        if (!resolved?.documentation?.value?.includes("Doubles it.")) problems.push(`completion documentation: ${JSON.stringify(resolved)}`);

        const target = await request("textDocument/definition", { textDocument: { uri }, position: { line: 3, character: 13 } });
        const wanted = { uri: pathToFileURL(lib).toString(), range: { start: { line: 3, character: 3 }, end: { line: 3, character: 8 } } };
        if (JSON.stringify(target) !== JSON.stringify([wanted])) problems.push(`definition across files: ${JSON.stringify(target)}`);

        published = diagnostics();
        send({ method: "textDocument/didChange", params: { textDocument: { uri, version: 2 }, contentChanges: [
            { range: { start: { line: 2, character: 14 }, end: { line: 2, character: 17 } }, text: "" }] } });
        found = await published;
        if (found.length !== 1 || found[0].severity !== 1 || !found[0].message.includes("needs a `do`")) problems.push(`syntax error: ${JSON.stringify(found)}`);

        const during = await request("textDocument/hover", { textDocument: { uri }, position: { line: 3, character: 13 } });
        if (!during?.contents?.value?.includes("Doubles it.")) problems.push(`hover while the file doesn't parse: ${JSON.stringify(during)}`);

        published = diagnostics();
        send({ method: "textDocument/didChange", params: { textDocument: { uri, version: 3 }, contentChanges: [{ text: 'use "gone.ec"\n/** @nope */\nfn main(): Int do\n    ret 0\n' }] } });
        found = await published;
        const messages = found.map(d => `${d.severity} ${d.message}`).join("\n");
        if (!/^1 File not found: 'gone.ec'/m.test(messages) || !/^2 Unknown ecdoc tag `@nope`/m.test(messages)) problems.push(`other problems: ${messages}`);

        // Type errors, from the text being edited rather than the file on disk.
        const typed = async (version: number, text: string) => {
            published = diagnostics();
            send({ method: "textDocument/didChange", params: { textDocument: { uri, version }, contentChanges: [{ text }] } });
            return (await published).map(d => `${d.severity} ${d.range.start.line}:${d.range.start.character} ${d.message}`).join("\n");
        };
        let said = await typed(4, 'fn main(): Int do\n    val x: Int = "a"c\n    ret x\n');
        if (!/^1 1:8 Cannot use CString as Int/.test(said) && !/^1 1:4 Cannot use CString as Int/.test(said)) problems.push(`type error: ${said}`);
        said = await typed(5, 'fn f(a: Int?): Int = a + 1\n\nfn main(): Int = f(1)\n');
        if (!/^1 0:/.test(said)) problems.push(`null error: ${said}`);
        said = await typed(6, "fn helper(): Int = 1\n");
        if (said !== "") problems.push(`a library without main: ${said}`);
        said = await typed(7, 'use "lib.ec"\n\nfn main(): Int = lib.twice(1)\n');
        if (said !== "") problems.push(`a good program: ${said}`);

        // A change in another open file, not saved, reaches the file that uses it.
        const libUri = pathToFileURL(lib).toString();
        const seen = new Promise<any[]>(done => {
            notes.length = 0;
            onNote = () => {
                const note = notes.find(n => n.method === "textDocument/publishDiagnostics" && n.params.uri === uri);
                if (note) done(note.params.diagnostics);
            };
        });
        send({ method: "textDocument/didOpen", params: { textDocument: { uri: libUri, languageId: "eclang", version: 1, text: 'module "lib"\n\nfn twice(x: Int): Bool = true\n' } } });
        const across = await seen;
        if (!across.some(d => d.severity === 1 && d.range.start.line === 2)) problems.push(`a change in a used file: ${JSON.stringify(across)}`);

        // An error in a used file shows on the `use`.
        const broken = new Promise<any[]>(done => {
            notes.length = 0;
            onNote = () => {
                const note = notes.find(n => n.method === "textDocument/publishDiagnostics" && n.params.uri === uri);
                if (note) done(note.params.diagnostics);
            };
        });
        send({ method: "textDocument/didChange", params: { textDocument: { uri: libUri, version: 2 }, contentChanges: [{ text: 'module "lib"\n\nfn twice(x: Int): Int = true\n' }] } });
        const onUse = await broken;
        if (!onUse.some(d => d.range.start.line === 0 && d.message.startsWith("lib.ec:3:"))) problems.push(`an error in a used file: ${JSON.stringify(onUse)}`);
        send({ method: "textDocument/didClose", params: { textDocument: { uri: libUri } } });

        published = diagnostics();
        send({ method: "textDocument/didClose", params: { textDocument: { uri } } });
        found = await published;
        if (found.length !== 0) problems.push("closing a file keeps its diagnostics");
        await request("shutdown", {});
    } finally {
        server.kill();
        rmSync(dir, { recursive: true, force: true });
    }
    report("language server", problems);
}

/** Every expected compile error must come through, and nothing that compiles may be flagged. */
async function testChecker(): Promise<void> {
    const problems: string[] = [];
    const checker = new Checker("java", jar, reason => problems.push(`unavailable: ${reason}`));
    let failing = 0, clean = 0;
    for (const file of ecFiles(join(repo, "tests/fail"))) {
        const expected = /^\/\/ error: (.*)$/m.exec(readFileSync(file, "utf8"))?.[1];
        if (expected === undefined || file.endsWith("no-main.ec")) continue; // a check doesn't ask for `main`
        const result = await checker.check(file, []);
        failing++;
        if (!result?.errors.some(e => e.message.includes(expected))) problems.push(`${file}: no '${expected}' in ${JSON.stringify(result)}`);
    }
    const project = new Project(join(root, "dist/core"), () => [], () => undefined);
    const good = ["tests/pass", "tests/panic", "src/main/resources"].flatMap(d => ecFiles(join(repo, d)));
    for (const file of good) {
        const name = project.compilerName(file);
        const result = await checker.check(name, [{ path: name, text: readFileSync(file, "utf8") }]);
        clean++;
        if (result === null || result.errors.length > 0) problems.push(`${file}: ${JSON.stringify(result?.errors ?? result)}`);
    }
    checker.stop();
    report("compiler errors", problems, `${failing} rejected, ${clean} accepted`);

    const gone: string[] = [];
    const missing = new Checker("/nonexistent/java", jar, reason => gone.push(reason));
    const answer = await missing.check(join(repo, "tests/pass/when.ec"), []);
    report("no Java", answer === null && gone.length === 1 && missing.available === false && gone[0].includes("eclang.java.path") ? [] : [`${answer} ${gone}`]);
}

/** Completion through the compiler: what fits, from the types it knows at the cursor, and without it from declarations. */
async function testCompletion(): Promise<void> {
    const problems: string[] = [];
    const dir = mkdtempSync(join(tmpdir(), "eclang-complete-"));
    const checker = new Checker("java", jar, reason => problems.push(`unavailable: ${reason}`));
    try {
        const file = join(dir, "main.ec");
        const base = `use "core:io.ec"

rec Point as
    x: Int
    y: Int

fn norm(p: Point): Int = p.x + p.y
fn label(p: Point): String = "point"
fn pick(mode: :fast | :slow): Int = 1

fn main(): Int do
    val p = Point { x = 1, y = 2 }
    val count = 3
    var name = "x"
LINE
    ret 0
`;
        let current = base;
        const project = new Project(join(root, "dist/core"), () => [dir], f => f === file ? current : undefined);
        /** The items at the `|` of [line], put in place of LINE, best first, with the compiler or without. */
        const at = async (line: string, compiler = true) => {
            const index = line.indexOf("|");
            const text = current = base.replace("LINE", line.replace("|", ""));
            const row = lines(text).findIndex(l => l === line.replace("|", ""));
            const place = placeAt(text, row, index);
            if (place === null) return null;
            const result = compiler ? await checker.complete(file, [{ path: file, text }], row + 1, index) : null;
            return { result, items: complete(project, file, text, place, result).sort((a, b) => a.sortText!.localeCompare(b.sortText!)) };
        };
        const labels = (found: Awaited<ReturnType<typeof at>>) => found?.items.map(i => i.label) ?? [];
        const first = (what: string, found: Awaited<ReturnType<typeof at>>, ...wanted: string[]) => {
            const got = labels(found).slice(0, wanted.length);
            if (JSON.stringify([...got].sort()) !== JSON.stringify([...wanted].sort())) problems.push(`${what}: expected ${wanted} first, got ${labels(found).slice(0, 8)}`);
        };
        const has = (what: string, found: Awaited<ReturnType<typeof at>>, ...wanted: string[]) => {
            for (const w of wanted) if (!labels(found).includes(w)) problems.push(`${what}: no '${w}' in ${labels(found).slice(0, 12)}`);
        };

        first("fields of the receiver's type first", await at("    val n: Int = p.|"), "x", "y");
        const methods = await at("    val n: Int = p.|");
        has("functions that take the receiver", methods, "norm", "label");
        const norm = methods?.items.find(i => i.label === "norm");
        if (norm?.insertText !== "norm()") problems.push(`a method without other arguments: ${JSON.stringify(norm)}`);
        if (labels(methods).indexOf("norm") > labels(methods).indexOf("label")) problems.push(`the result that fits first: ${labels(methods)}`);
        has("functions of String", await at("    val n = name.|"), "len", "startsWith", "contains");
        first("what has the expected type first", await at("    val n: Int = c|"), "count");
        first("the atoms the parameter takes", await at("    val n = pick(:|)"), ":fast", ":slow");
        has("a module's functions", await at("    io.|"), "log", "readFile");
        has("types", await at("    var q: Po|"), "Point", "Int", "String");
        has("locals, with the line unfinished", await at("    val n = norm(|"), "p");
        first("a local of the expected type", await at("    val n = norm(|"), "p");
        has("keywords at the start of a statement", await at("    re|"), "ret");
        if (await at("    val na|") !== null) problems.push("a name being declared gets completions");
        if (await at('    io.log("p.|")') !== null) problems.push("text in a string gets completions");
        if (await at("    // p.|") !== null) problems.push("a comment gets completions");

        // Without the compiler: names from declarations, from the last time the file parsed (here, before the line was typed).
        current = base.replace("LINE", "    pass");
        project.docs(file);
        has("without the compiler, locals and functions", await at("    val n = |", false), "count", "norm", "Point", "io");
        has("without the compiler, a module's functions", await at("    io.|", false), "log");
    } finally {
        checker.stop();
        rmSync(dir, { recursive: true, force: true });
    }
    report("completion", problems);
}

async function main() {
    compareWithCompiler();
    await testChecker();
    await testCompletion();
    testHover();
    testDefinition();
    testRanges();
    await testHighlighting();
    await testServer();
    console.log(failures === 0 ? "all passed" : `${failures} failed`);
    process.exit(failures === 0 ? 0 : 1);
}
main();
