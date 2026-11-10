import { CompletionItem, CompletionItemKind, InsertTextFormat, MarkupKind } from "vscode-languageserver";
import { CompilerItem, CompleteResult } from "./checker";
import { DocItem, DocKind } from "./docs";
import { allMembers, documentation, moduleDeclarations, moduleNames, visibleDeclarations } from "./hover";
import { lines } from "./kotlin";
import { Project } from "./project";

// Completion. The compiler decides what fits (see Completion.kt): it knows the types of locals, what is expected where the
// cursor is and the type of `x` in `x.`. What it can't answer (no Java, a file that doesn't parse, a place it never compiles)
// gets the names this server knows from declarations, as hovers do, without types.

/** Where completion was asked for: [start] to [end] of [line] (0-based) is the word being typed (with its `:` for an atom). */
export interface Place {
    line: number;
    character: number;
    start: number;
    end: number;
    atom: boolean;
    /** It comes after a `.`; [qualifier] is the plain name before that, if any. */
    dotted: boolean;
    qualifier: string | null;
    /** The word is the first of its line, and the line isn't indented when [topLevel]. */
    lineStart: boolean;
    topLevel: boolean;
    /** A `(` follows the word, so a function needs no parentheses of its own. */
    callFollows: boolean;
}

const isIdent = (c: string | undefined) => c !== undefined && /[A-Za-z0-9_]/.test(c);

/** Whether [prefix], the start of a line, ends inside a comment, or inside a string but not in its `${...}`. */
function inCommentOrText(prefix: string): boolean {
    const trimmed = prefix.trimStart();
    if (trimmed.startsWith("*") || trimmed.startsWith("/*")) return true; // a line of a block comment or an ecdoc
    let inString = false, depth = 0;
    for (let i = 0; i < prefix.length; i++) {
        const c = prefix[i];
        if (inString && depth === 0) {
            if (c === "\\") i++;
            else if (c === '"') inString = false;
            else if (c === "$" && prefix[i + 1] === "{") { depth = 1; i++; }
        } else if (inString) {
            if (c === "{") depth++;
            else if (c === "}") depth--;
        } else if (c === '"') inString = true;
        else if (c === "'") i += prefix[i + 1] === "\\" ? 3 : 2; // a Char
        else if (c === "/" && prefix[i + 1] === "/") return true;
    }
    return inString && depth === 0;
}

/** Where completion at [line] and [character] (both 0-based) of [text] completes, or null when nothing should be offered there. */
export function placeAt(text: string, line: number, character: number): Place | null {
    const current = lines(text)[line];
    if (current === undefined) return null;
    const at = Math.min(character, current.length);
    let start = at, end = at;
    while (start > 0 && isIdent(current[start - 1])) start--;
    while (end < current.length && isIdent(current[end])) end++;
    const before = current.slice(0, start);
    if (inCommentOrText(before) || /^[0-9]/.test(current.slice(start, at))) return null;
    // A name being declared: `val x`, `fn name`, `for i, x`, a parameter `fn f(a, b`.
    if (/(^|[^\w.])(val|var|const|fn|rec|inter|type|module|enum|for|catch|union|struct)\s+$/.test(before) || /\bfor\s+\w+\s*,\s*$/.test(before)
        || /\bfn\s+\w+\s*(<[^>]*>)?\s*\(([^()]*,)?\s*((mut|out|own|flat|escaping|kept)\s+)?$/.test(before)) return null;
    const atom = before.endsWith(":") && !/[\w:)\]>?]$/.test(before.slice(0, -1));
    const dotted = before.endsWith(".") && !before.endsWith("..");
    const qualifier = dotted ? /(?:^|[^\w.?!)\]])([A-Za-z_]\w*)\??\.$/.exec(before)?.[1] ?? null : null;
    const lineStart = before.trim().length === 0;
    return { line, character: at, start: atom ? start - 1 : start, end, atom, dotted, qualifier, lineStart, topLevel: lineStart && before.length === 0,
        callFollows: current.slice(end).trimStart().startsWith("(") };
}

const STATEMENT_KEYWORDS = ["val", "var", "const", "if", "else", "while", "for", "when", "ret", "defer", "errdefer", "try", "throw", "assert",
    "break", "continue", "pass", "yield", "unsafe", "destroy", "pool", "debug"];
const DECLARATION_KEYWORDS = ["fn", "rec", "inter", "type", "use", "module", "declare", "export", "priv", "const", "val", "var", "inline", "mut",
    "ref", "resource", "sync", "union", "struct", "enum"];
const EXPRESSION_KEYWORDS = ["true", "false", "null", "new", "not", "and", "or", "xor", "is", "as", "in", "to", "sizeof", "alignof", "offsetof",
    "typeof", "addrof", "try", "if", "when", "unsafe", "here", "pool", "it"];

/** Keywords that fit at [place], ranked after the names that fit the expected type. */
function keywords(place: Place): CompletionItem[] {
    const words = place.dotted || place.atom ? [] : [
        ...(place.topLevel ? DECLARATION_KEYWORDS : place.lineStart ? STATEMENT_KEYWORDS : []),
        ...(place.topLevel ? [] : EXPRESSION_KEYWORDS)];
    return [...new Set(words)].map(word => ({ label: word, kind: CompletionItemKind.Keyword, sortText: sortText(place.lineStart ? 4 : 8, word) }));
}

const sortText = (rank: number, label: string) => `${String(Math.min(rank, 99)).padStart(2, "0")}_${label}`;

const COMPILER_KINDS: Record<string, CompletionItemKind> = {
    field: CompletionItemKind.Field, method: CompletionItemKind.Method, function: CompletionItemKind.Function,
    variable: CompletionItemKind.Variable, value: CompletionItemKind.Variable, parameter: CompletionItemKind.Variable,
    constant: CompletionItemKind.Constant, record: CompletionItemKind.Struct, interface: CompletionItemKind.Interface,
    enum: CompletionItemKind.Enum, type: CompletionItemKind.Class, typeParameter: CompletionItemKind.TypeParameter,
    module: CompletionItemKind.Module, atom: CompletionItemKind.EnumMember,
};

const DOC_KINDS: Record<DocKind, CompletionItemKind> = {
    FUNCTION: CompletionItemKind.Function, C_FUNCTION: CompletionItemKind.Function, RECORD: CompletionItemKind.Struct,
    C_RECORD: CompletionItemKind.Struct, C_STRUCT: CompletionItemKind.Struct, INTERFACE: CompletionItemKind.Interface,
    TYPE: CompletionItemKind.Class, ENUM: CompletionItemKind.Enum, CONSTANT: CompletionItemKind.Constant, VALUE: CompletionItemKind.Constant,
    VARIABLE: CompletionItemKind.Variable, C_VALUE: CompletionItemKind.Constant, FIELD: CompletionItemKind.Field,
    METHOD: CompletionItemKind.Method, ENUM_VALUE: CompletionItemKind.EnumMember,
};

/** What [CompletionItem.data] holds, to find the documentation when VS Code asks for it ([resolve]). */
interface Data { file: string; name: string; module: string | null; member: boolean; }

/** `name()`, or `name(|)` with the cursor inside, unless a `(` is there already. */
function callable(item: CompletionItem, name: string, params: number | undefined, place: Place): void {
    if (place.callFollows || params === undefined) return;
    if (params === 0) item.insertText = `${name}()`;
    else {
        item.insertText = `${name}($0)`;
        item.insertTextFormat = InsertTextFormat.Snippet;
        item.command = { title: "Signature", command: "editor.action.triggerParameterHints" };
    }
}

function fromCompiler(file: string, place: Place, result: CompleteResult): CompletionItem[] {
    const items = result.items ?? [];
    const best = result.expected !== undefined ? items.find(i => i.rank < 10) : undefined;
    return items.map((c: CompilerItem): CompletionItem => {
        const isFunction = c.kind === "function" || c.kind === "method";
        const item: CompletionItem = {
            label: c.label,
            kind: COMPILER_KINDS[c.kind] ?? CompletionItemKind.Text,
            detail: c.detail,
            labelDetails: c.module !== undefined && result.context !== "module" ? { description: c.module } : undefined,
            sortText: sortText(c.rank, c.label),
            preselect: c === best || undefined,
            data: { file, name: c.label, module: c.module ?? null, member: c.kind === "field" } satisfies Data,
        };
        if (c.kind === "atom") {
            // VS Code's word stops at the `:`, so the item says what it replaces.
            const range = { start: { line: place.line, character: place.start }, end: { line: place.line, character: place.character } };
            item.textEdit = { range, newText: c.label };
            item.filterText = c.label;
        }
        if (isFunction) callable(item, c.label, c.params, place);
        return item;
    });
}

function fromDocs(file: string, place: Place, items: { item: DocItem; module: string | null }[], rank: number): CompletionItem[] {
    const seen = new Set<string>();
    return items.flatMap(({ item, module }) => {
        if (seen.has(item.name + " " + item.kind)) return [];
        seen.add(item.name + " " + item.kind);
        const result: CompletionItem = { label: item.name, kind: DOC_KINDS[item.kind], detail: item.signature, sortText: sortText(rank, item.name),
            data: { file, name: item.name, module, member: item.kind === "FIELD" } satisfies Data };
        if (item.kind === "FUNCTION" || item.kind === "C_FUNCTION") callable(result, item.name, /\(\s*\)/.test(item.signature) ? 0 : 1, place);
        return [result];
    });
}

/** What the server can offer without the compiler: names, from the declarations it knows. */
function withoutCompiler(project: Project, file: string, text: string, place: Place): CompletionItem[] {
    if (place.atom) {
        const atoms = [...new Set([...text.matchAll(/(?<![\w:)\]>?]):([A-Za-z0-9]\w*)/g)].map(m => ":" + m[1]))];
        const range = { start: { line: place.line, character: place.start }, end: { line: place.line, character: place.character } };
        return atoms.map(a => ({ label: a, kind: CompletionItemKind.EnumMember, textEdit: { range, newText: a }, filterText: a }));
    }
    const modules = moduleNames(project, file);
    if (place.dotted) {
        if (place.qualifier !== null && modules.includes(place.qualifier))
            return fromDocs(file, place, moduleDeclarations(project, file, place.qualifier).map(item => ({ item, module: place.qualifier })), 3);
        // Without the type of what's before the dot, every field, and every function that takes something.
        const functions = visibleDeclarations(project, file).filter(d => (d.item.kind === "FUNCTION" || d.item.kind === "C_FUNCTION") && !/\(\s*\)/.test(d.item.signature));
        return [...fromDocs(file, place, allMembers(project, file).map(item => ({ item, module: null })), 0), ...fromDocs(file, place, functions, 3)];
    }
    const docs = project.docs(file);
    const scope = docs?.scopes.find(s => s.firstLine <= place.line + 1 && place.line + 1 <= s.lastLine);
    const locals: CompletionItem[] = scope === undefined ? [] : [
        ...[...scope.params].map(([name, p]) => ({ label: name, kind: CompletionItemKind.Variable, detail: p.written, sortText: sortText(0, name) })),
        ...[...scope.locals].filter(([, at]) => at.some(l => l.line <= place.line + 1)).map(([name]) => ({ label: name, kind: CompletionItemKind.Variable, sortText: sortText(0, name) })),
    ];
    return [
        ...locals,
        ...fromDocs(file, place, visibleDeclarations(project, file), 3),
        ...modules.map(m => ({ label: m, kind: CompletionItemKind.Module, detail: `module "${m}"`, sortText: sortText(6, m) })),
        ...keywords(place),
    ];
}

/** The completion items at [place] of [file] (whose text is [text]), from what the compiler found there ([result]) when it could tell. */
export function complete(project: Project, file: string, text: string, place: Place, result: CompleteResult | null): CompletionItem[] {
    if (result === null || !result.found) return withoutCompiler(project, file, text, place);
    return [...fromCompiler(file, place, result), ...(result.context === "name" ? keywords(place) : [])];
}

/** [item] with its documentation, from the ecdoc of its declaration. */
export function resolve(project: Project, item: CompletionItem): CompletionItem {
    const data = item.data as Data | undefined;
    if (data === undefined) return item;
    const value = documentation(project, data.file, data.name, data.module, data.member);
    return value === null ? item : { ...item, documentation: { kind: MarkupKind.Markdown, value } };
}
