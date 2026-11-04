import { basename, join } from "node:path";
import { fileURLToPath } from "node:url";
import { createConnection, Diagnostic, DiagnosticSeverity, ProposedFeatures, TextDocuments, TextDocumentSyncKind } from "vscode-languageserver/node";
import { TextDocument } from "vscode-languageserver-textdocument";
import { Checker, CheckProblem, sameFile } from "./checker";
import { diagnose, rangeAt } from "./diagnostics";
import { definition, hover } from "./hover";
import { lines } from "./kotlin";
import { Project } from "./project";

/** How long after the last keystroke a file is checked, in milliseconds. */
const DEBOUNCE = 150;

const connection = createConnection(ProposedFeatures.all);
const documents = new TextDocuments(TextDocument);
const uriOfFile = new Map<string, string>();
let roots: string[] = [];
let checker: Checker | null = null;

/** The name of the file at [uri], see `Project`. */
const fileOf = (uri: string) => uri.startsWith("file:") ? fileURLToPath(uri) : uri;

const project = new Project(join(__dirname, "core"), () => roots, file => {
    const uri = uriOfFile.get(file);
    return uri === undefined ? undefined : documents.get(uri)?.getText();
});

connection.onInitialize(params => {
    roots = (params.workspaceFolders ?? []).map(f => f.uri).filter(u => u.startsWith("file:")).map(fileOf);
    const options = params.initializationOptions ?? {};
    if (options.typeCheck !== false)
        checker = new Checker(options.javaPath || "java", options.compilerJar || join(__dirname, "eclang.jar"), reason => {
            connection.window.showWarningMessage(`eclang: type errors won't be shown, only syntax errors. ${reason}`);
            connection.console.error(reason);
            for (const document of documents.all()) check(document); // what the server can tell without the compiler
        });
    return { capabilities: { textDocumentSync: TextDocumentSyncKind.Incremental, hoverProvider: true, definitionProvider: true } };
});

const pending = new Map<string, NodeJS.Timeout>();

/** Files waiting for the compiler, the next one first. */
const queue: string[] = [];
let compiling = false;

function check(document: TextDocument): void {
    const uri = document.uri;
    clearTimeout(pending.get(uri));
    pending.set(uri, setTimeout(() => {
        pending.delete(uri);
        const open = documents.get(uri);
        if (open === undefined) return;
        // A file that doesn't parse or a checker that's gone is all the server can say, and it can say it at once. Otherwise
        // what's shown stays until the compiler answers, so that errors don't blink off at each keystroke.
        if (project.parsed(fileOf(uri))?.docs === null || !canCompile(uri)) publishParsed(open);
        // Other files may use this one, so they are checked again too.
        for (const other of [open, ...documents.all().filter(d => d.uri !== uri)]) {
            if (project.parsed(fileOf(other.uri))?.docs && canCompile(other.uri) && !queue.includes(other.uri)) queue.push(other.uri);
        }
        void compileNext();
    }, DEBOUNCE));
}

const canCompile = (uri: string) => checker?.available === true && uri.startsWith("file:");

function publishParsed(document: TextDocument): void {
    const diagnostics = diagnose(project, fileOf(document.uri), document.getText());
    connection.sendDiagnostics({ uri: document.uri, version: document.version, diagnostics });
}

async function compileNext(): Promise<void> {
    if (compiling) return;
    compiling = true;
    try {
        for (let uri = queue.shift(); uri !== undefined; uri = queue.shift()) {
            const document = documents.get(uri);
            if (document === undefined || checker === null) continue;
            const { version } = document;
            const unsaved = documents.all().filter(d => d.uri.startsWith("file:")).map(d => ({ path: project.compilerName(fileOf(d.uri)), text: d.getText() }));
            const result = await checker.check(project.compilerName(fileOf(uri)), unsaved);
            const now = documents.get(uri);
            if (now === undefined || now.version !== version) continue; // it changed, so it's on its way back here
            if (result === null) publishParsed(now);
            else connection.sendDiagnostics({ uri, version, diagnostics: compilerDiagnostics(now, result.errors, result.warnings) });
        }
    } finally {
        compiling = false;
    }
}

/** What the compiler said, for [document]. An error in a file it uses goes on the `use`: it may be why nothing else is reported. */
function compilerDiagnostics(document: TextDocument, errors: CheckProblem[], warnings: CheckProblem[]): Diagnostic[] {
    const file = fileOf(document.uri);
    const source = lines(document.getText());
    const name = project.compilerName(file);
    const here = (p: CheckProblem) => p.file === name || sameFile(p.file, file);
    const diagnostic = (p: CheckProblem, severity: DiagnosticSeverity): Diagnostic =>
        ({ range: rangeAt(source, { line: p.line, column: p.column }), severity, message: p.message, source: "eclang" });

    const elsewhere = new Map<string, CheckProblem>();
    for (const e of errors) if (!here(e) && !elsewhere.has(e.file)) elsewhere.set(e.file, e);
    const uses = project.docs(file)?.uses ?? [];
    const leadsTo = (target: string) => uses.find(u => {
        const used = u.path.endsWith(".ec") ? project.resolve(file, u) : null;
        return used !== null && project.closure(used).some(f => sameFile(target, f.file));
    });
    return [
        ...errors.filter(here).map(e => diagnostic(e, DiagnosticSeverity.Error)),
        ...warnings.filter(here).map(w => diagnostic(w, DiagnosticSeverity.Warning)),
        ...[...elsewhere.values()].map(e => ({
            range: rangeAt(source, leadsTo(e.file)?.location ?? { line: 1, column: 0 }),
            severity: DiagnosticSeverity.Error,
            message: `${basename(e.file)}:${e.line}: ${e.message}`,
            source: "eclang",
        })),
    ];
}

documents.onDidOpen(e => { uriOfFile.set(fileOf(e.document.uri), e.document.uri); });
documents.onDidSave(e => check(e.document)); // files that use it may read it from disk
documents.onDidChangeContent(e => {
    uriOfFile.set(fileOf(e.document.uri), e.document.uri);
    check(e.document);
});
documents.onDidClose(e => {
    const uri = e.document.uri;
    clearTimeout(pending.get(uri));
    pending.delete(uri);
    uriOfFile.delete(fileOf(uri));
    project.close(fileOf(uri));
    connection.sendDiagnostics({ uri, diagnostics: [] });
});

connection.onHover(params => {
    const document = documents.get(params.textDocument.uri);
    if (document === undefined) return null;
    const { line, character } = params.position;
    const value = hover(project, fileOf(document.uri), document.getText(), line, character);
    return value === null ? null : { contents: { kind: "markdown", value } };
});

connection.onDefinition(params => {
    const document = documents.get(params.textDocument.uri);
    if (document === undefined) return null;
    const { line, character } = params.position;
    return definition(project, fileOf(document.uri), document.getText(), line, character).map(d => {
        const start = { line: d.location.line - 1, character: d.location.column };
        return { uri: project.uriOf(d.file), range: { start, end: { ...start, character: start.character + d.length } } };
    });
});

connection.onShutdown(() => checker?.stop());

documents.listen(connection);
connection.listen();
