import { Diagnostic, DiagnosticSeverity, Range } from "vscode-languageserver";
import { Loc } from "./ecdoc";
import { lines } from "./kotlin";
import { Project } from "./project";

/** The word at [at], the rest of the line when there's none, or the end of the line: the compiler only knows a point. */
export function rangeAt(source: string[], at: Loc): Range {
    const line = Math.max(0, Math.min(at.line - 1, source.length - 1));
    const text = source[line] ?? "";
    const point = (start: number, end: number): Range => ({ start: { line, character: start }, end: { line, character: end } });
    const column = Math.min(at.column, text.length);
    const word = /^\w+/.exec(text.slice(column));
    if (word !== null) {
        const before = /\w*$/.exec(text.slice(0, column))![0].length;
        return point(column - before, column + word[0].length);
    }
    const rest = text.slice(column);
    if (rest.trim().length > 0) {
        if (column === 0 || /^\s/.test(rest)) return point(column + rest.search(/\S/), text.trimEnd().length); // a whole line
        return point(column, column + 1);
    }
    return column > 0 ? point(column - 1, column) : point(0, 0);
}

function diagnostic(source: string[], at: Loc, severity: DiagnosticSeverity, message: string): Diagnostic {
    return { range: rangeAt(source, at), severity, message, source: "eclang" };
}

/** The first syntax error of [file], or the problems in its ecdocs and the `use`s that lead nowhere. */
export function diagnose(project: Project, file: string, text: string): Diagnostic[] {
    const source = lines(text);
    const parsed = project.parsed(file);
    if (parsed === null) return [];
    if (parsed.docs === null) return [diagnostic(source, parsed.error, DiagnosticSeverity.Error, parsed.error.message)];
    const missing = parsed.docs.uses.filter(u => u.path.endsWith(".ec") && project.resolve(file, u) === null)
        .map(u => diagnostic(source, u.location, DiagnosticSeverity.Error, `File not found: '${u.path}'.`));
    return [...missing, ...parsed.docs.problems.map(p => diagnostic(source, p.location, DiagnosticSeverity.Warning, p.message))];
}
