import { Token } from "antlr4";
import EclangLexer from "./antlr/EclangLexer";
import { DocItem, FileDocs, FunctionScope } from "./docs";
import { AUTHOR, DEPRECATED, DocTag, Ecdoc, Loc, PARAM, RETURN, SEE, SINCE, THROWS } from "./ecdoc";
import { tokenize } from "./parse";
import { Project } from "./project";

/** How many overloads a tooltip shows. */
const MAX_SHOWN = 6;

/** The name under the cursor, and what's around it. */
interface Target {
    token: Token;
    /** It comes after a `.`, and this is the name before that (when it's a plain name, not `a.b.c` or `f().c`). */
    dotted: boolean;
    qualifier: string | null;
    /** `name =` inside `{ ... }`: a field of a record literal. */
    fieldInit: boolean;
    /** `name(`: a call. */
    call: boolean;
    /** Inside `${...}`: the position of [token] is relative to the interpolation, not the file. */
    embedded: boolean;
}

/** A file and its declarations. */
interface Source { file: string; docs: FileDocs; }

/** A declaration found for a name, in [file]. [owner] is the type a member belongs to. */
interface Found { item: DocItem; file: string; module: string | null; owner: DocItem | null; }

/** What a name stands for. */
type Resolved =
    | { kind: "declarations"; found: Found[] }
    | { kind: "parameter"; name: string; scope: FunctionScope }
    | { kind: "local"; declared: Loc[] }
    | { kind: "module"; name: string; files: Source[] };

/** Where something is declared: [length] characters at [location] of [file]. */
export interface Definition { file: string; location: Loc; length: number; }

const isLayout = (t: Token | undefined) => t?.type === EclangLexer.NL;

function contains(t: Token, line: number, column: number): boolean {
    return t.line === line && t.column <= column && column < t.column + t.text.length;
}

/** The target at [line] (1-based) and [column] of [text]: inside `${...}` of a string too. */
function targetAt(text: string, line: number, column: number): Target | null {
    const tokens = tokenize(text);
    const i = tokens.findIndex(t => contains(t, line, column));
    if (i < 0) return null;
    const token = tokens[i];
    if (token.type === EclangLexer.STRING) {
        const offset = column - token.column;
        for (const m of token.text.matchAll(/(?<!\\)\$\{([^}]*)}/g)) {
            const start = m.index + 2;
            if (start <= offset && offset < start + m[1].length) {
                const inner = targetAt(m[1], 1, offset - start);
                return inner && { ...inner, embedded: true };
            }
        }
        return null;
    }
    if (token.type !== EclangLexer.NAME && token.type !== EclangLexer.ATOM) return null;
    const before = (n: number) => { // the n-th token before this one, not counting line ends
        let j = i;
        for (let k = 0; k < n; k++) do j--; while (isLayout(tokens[j]));
        return tokens[j];
    };
    const dotted = before(1)?.text === "." || before(1)?.text === "?.";
    const plainQualifier = dotted && before(2)?.type === EclangLexer.NAME && before(3)?.text !== "." && before(3)?.text !== "?.";
    const next = tokens[i + 1];
    return { token, dotted, qualifier: plainQualifier ? before(2).text : null, call: next?.text === "(", embedded: false,
        fieldInit: next?.text === "=" && (before(1)?.text === "{" || before(1)?.text === ",") };
}

const typesOf = (docs: FileDocs) => docs.items.filter(i => i.members.length > 0);

function declaredAt({ file, docs }: Source, t: Token): Found | null {
    const here = (i: DocItem) => i.location.line === t.line && i.location.column === t.column;
    for (const item of docs.items) {
        if (here(item)) return { item, file, module: docs.module, owner: null };
        const member = item.members.find(here);
        if (member !== undefined) return { item: member, file, module: docs.module, owner: item };
    }
    return null;
}

function membersNamed(files: Source[], name: string): Found[] {
    return files.flatMap(({ file, docs }) => typesOf(docs).flatMap(owner =>
        owner.members.filter(m => m.name === name).map(item => ({ item, file, module: docs.module, owner }))));
}

function itemsNamed(files: Source[], name: string, module: string | null, here: string | null): Found[] {
    return files.filter(f => f.docs.module === module).flatMap(({ file, docs }) =>
        docs.items.filter(i => i.name === name && (!i.isPrivate || module === here)).map(item => ({ item, file, module, owner: null })));
}

/** The declarations [target] may stand for. There's no type information here, so `x.name` matches every `name`. */
function resolve(target: Target, name: string, files: Source[], current: Source, scope: FunctionScope | undefined): Resolved | null {
    const declarations = (found: Found[]): Resolved => ({ kind: "declarations", found });
    if (target.token.type === EclangLexer.ATOM) return declarations(membersNamed(files, name).filter(f => f.item.kind === "ENUM_VALUE"));

    const declared = target.embedded ? null : declaredAt(current, target.token);
    if (declared !== null) return declarations([declared]);

    const isLocal = (n: string) => scope !== undefined && (scope.params.has(n) || scope.locals.has(n));
    if (target.fieldInit) {
        const fields = membersNamed(files, name).filter(f => f.item.kind === "FIELD");
        if (fields.length > 0) return declarations(fields);
    }
    if (!target.dotted && !target.call && scope !== undefined) {
        if (scope.params.has(name)) return { kind: "parameter", name, scope };
        const locals = scope.locals.get(name);
        if (locals !== undefined) return { kind: "local", declared: locals };
    }

    const here = current.docs.module;
    if (target.qualifier !== null && !isLocal(target.qualifier)) {
        const q = target.qualifier;
        if (files.some(f => f.docs.module === q)) return declarations(itemsNamed(files, name, q, here));
        const members = membersNamed(files, name).filter(f => f.owner?.name === q);
        if (members.length > 0) return declarations(members);
    }
    if (target.dotted) { // a field, or `x.f(y)`, which is `f(x, y)`
        const functions = [...new Set([here, null])].flatMap(m => itemsNamed(files, name, m, here));
        return declarations([...membersNamed(files, name), ...functions]);
    }

    const visible = [...new Set([here, null])].flatMap(m => itemsNamed(files, name, m, here));
    if (visible.length > 0) return declarations(visible);
    const module = files.filter(f => f.docs.module === name);
    return module.length > 0 ? { kind: "module", name, files: module } : null;
}

/** Replaces `{@link name label}`, `{@linkplain name label}` and `{@code text}`. */
function inline(text: string): string {
    return text.replace(/\{@(link|linkplain|code)\s+([^}]*)}/g, (_, kind: string, written: string) => {
        const body = written.trim();
        if (kind === "code") return `\`${body}\``;
        const [ref, ...label] = body.split(" ");
        const shown = label.join(" ").trim();
        if (shown.length > 0) return shown;
        const name = ref.replace(/\(.*$/, "").replace(/^#/, "");
        return kind === "link" ? `\`${name}\`` : name;
    });
}

/** `@see name`, `@see name label`, `@see "text"`, `@see <a ...>` or `@see [label](url)`. */
function see(text: string): string {
    if (text.startsWith("\"")) return text.replace(/^"+|"+$/g, "");
    if (text.startsWith("http")) return `<${text}>`;
    if (text.startsWith("<") || text.startsWith("[")) return text;
    const [ref, ...label] = text.split(" ");
    return label.length > 0 ? label.join(" ").trim() : `\`${ref.replace(/\(.*$/, "").replace(/^#/, "")}\``;
}

const bullet = (head: string, text: string) => {
    const body = inline(text).trim();
    return body.length === 0 ? `- ${head}` : `- ${head} — ${body.replace(/\n/g, "\n  ")}`;
};
const code = (signature: string) => "```eclang\n" + signature + "\n```";

function tagList(title: string, tags: DocTag[], head: (t: DocTag) => string): string[] {
    return tags.length === 0 ? [] : [`**${title}**`, tags.map(t => bullet(head(t), t.text)).join("\n")];
}

function footer(doc: Ecdoc): string[] {
    const since = doc.first(SINCE), authors = doc.all(AUTHOR), seeAlso = doc.all(SEE);
    const lines = [
        ...(since ? [`**Since** ${inline(since.text)}`] : []),
        ...(authors.length > 0 ? [`**Author** ${authors.map(a => inline(a.text)).join(", ")}`] : []),
        ...(seeAlso.length > 0 ? [`**See also** ${seeAlso.map(s => see(s.text)).join(", ")}`] : []),
    ];
    return lines.length === 0 ? [] : [lines.join("  \n")];
}

function render({ item, module, owner }: Found): string {
    const doc = item.doc;
    const thrown = item.signature.includes(" throws ") ? item.signature.slice(item.signature.lastIndexOf(" throws ") + 8) : "Error";
    const where = owner !== null ? `${owner.kind === "ENUM" ? "Value" : "Member"} of \`${owner.name}\`` : module !== null ? `In module \`${module}\`` : null;
    const notes = [where, item.isPrivate ? "Private to its module (`priv`)" : null, item.cName !== null ? `Binds the C function \`${item.cName}\`` : null]
        .filter(n => n !== null);
    const deprecated = doc?.first(DEPRECATED);
    const params = doc?.all(PARAM).filter(p => p.arg !== null) ?? [];
    const returns = doc?.first(RETURN);
    return [
        code([item.signature, ...item.contracts.map(c => `    ${c}`)].join("\n")),
        ...(deprecated ? [`> **Deprecated.** ${inline(deprecated.text)}`.trimEnd()] : []),
        ...(doc && doc.description.length > 0 ? [inline(doc.description)] : []),
        ...(notes.length > 0 ? [notes.map(n => `*${n}.*`).join(" ")] : []),
        ...tagList("Parameters", params, p => `\`${p.arg}\``),
        ...(returns ? [`**Returns** ${inline(returns.text)}`.trimEnd()] : []),
        ...tagList("Throws", doc?.all(THROWS) ?? [], t => `\`${t.arg ?? thrown}\``),
        ...(item.members.length > 0 ? ["**" + (item.kind === "ENUM" ? "Values" : "Members") + "**", item.members.map(m => {
            const gone = m.doc?.first(DEPRECATED);
            return bullet(`\`${m.signature}\``, [gone ? `**Deprecated.** ${gone.text}` : "", m.doc?.description ?? ""].filter(s => s.length > 0).join(" "));
        }).join("\n")] : []),
        ...(doc ? footer(doc) : []),
    ].join("\n\n");
}

function paramHover(name: string, scope: FunctionScope): string {
    const tag = scope.item.doc?.all(PARAM).find(p => p.arg === name);
    return [code(scope.params.get(name)!.written), `*Parameter of \`${scope.item.name}\`.*`, ...(tag && tag.text.length > 0 ? [inline(tag.text)] : [])].join("\n\n");
}

function moduleHover(name: string, files: Source[]): string {
    const overview = files.find(f => f.docs.overview !== null)?.docs.overview;
    return [code(`module "${name}"`), ...(overview ? [inline(overview.description), ...footer(overview)] : [])].filter(s => s.length > 0).join("\n\n");
}

/** What the name at [line] and [character] (both 0-based) of [text], the contents of [file], stands for, or null. */
function lookup(project: Project, file: string, text: string, line: number, character: number): Resolved | null {
    const target = targetAt(text, line + 1, character);
    if (target === null) return null;
    const docs = project.docs(file);
    if (docs === null) return null;
    const scope = docs.scopes.find(s => s.firstLine <= line + 1 && line + 1 <= s.lastLine);
    return resolve(target, target.token.text, project.closure(file), { file, docs }, scope);
}

/** The Markdown for a hover at [line] and [character] (both 0-based) of [text], the contents of [file], or null. */
export function hover(project: Project, file: string, text: string, line: number, character: number): string | null {
    const resolved = lookup(project, file, text, line, character);
    switch (resolved?.kind) {
        case "parameter": return paramHover(resolved.name, resolved.scope);
        case "module": return moduleHover(resolved.name, resolved.files);
        case "declarations": {
            const { found } = resolved;
            if (found.length === 0) return null;
            const shown = found.slice(0, MAX_SHOWN).map(render);
            if (found.length > MAX_SHOWN) shown.push(`*And ${found.length - MAX_SHOWN} more.*`);
            return shown.join("\n\n---\n\n");
        }
        default: return null; // a local has nothing to show: no ecdoc, and no type without compiling
    }
}

/** The declaration of a local that reaches [line] (1-based): the last one before it, as blocks can reuse a name. */
function nearest(declared: Loc[], line: number): Loc {
    const before = declared.filter(l => l.line <= line);
    return before.length > 0 ? before.reduce((a, b) => b.line >= a.line ? b : a) : declared[0];
}

/** Where the name (or the `use` path) at [line] and [character] (both 0-based) of [text], the contents of [file], is declared. */
export function definition(project: Project, file: string, text: string, line: number, character: number): Definition[] {
    const use = project.docs(file)?.uses.find(u => u.location.line === line + 1
        && u.location.column <= character && character < u.location.column + u.path.length + 2);
    if (use !== undefined) {
        const target = use.path.endsWith(".ec") ? project.resolve(file, use) : null;
        return target === null ? [] : [{ file: target, location: { line: 1, column: 0 }, length: 0 }];
    }
    const resolved = lookup(project, file, text, line, character);
    switch (resolved?.kind) {
        case "declarations": return resolved.found.map(f => ({ file: f.file, location: f.item.location, length: f.item.name.length }));
        case "parameter": return [{ file, location: resolved.scope.params.get(resolved.name)!.location, length: resolved.name.length }];
        case "local": {
            const name = targetAt(text, line + 1, character)!.token.text;
            return [{ file, location: nearest(resolved.declared, line + 1), length: name.length }];
        }
        case "module": return resolved.files.map(f => ({ file: f.file, location: f.docs.moduleLocation!, length: resolved.name.length + 2 }));
        default: return [];
    }
}

/**
 * The documentation of [name], as its hover shows it, for a completion item in [file]: a member of a type (a field) when
 * [member], otherwise a declaration of module [module] (null: the top level). Of overloads, the first. Null when there's none.
 */
export function documentation(project: Project, file: string, name: string, module: string | null, member: boolean): string | null {
    const files = project.closure(file);
    const found = member ? membersNamed(files, name) : itemsNamed(files, name, module, project.docs(file)?.module ?? null);
    return found.length === 0 ? null : render(found[0]);
}

/** The declarations of [file] and the files it uses that code in [file] sees, for completion without the compiler. */
export function visibleDeclarations(project: Project, file: string): { item: DocItem; module: string | null }[] {
    const here = project.docs(file)?.module ?? null;
    return project.closure(file).filter(f => f.docs.module === here || f.docs.module === null)
        .flatMap(f => f.docs.items.filter(i => !i.isPrivate || f.docs.module === here).map(item => ({ item, module: f.docs.module })));
}

/** The declarations of module [module] that code in [file] sees, for completion without the compiler. */
export function moduleDeclarations(project: Project, file: string, module: string): DocItem[] {
    const here = project.docs(file)?.module ?? null;
    return project.closure(file).filter(f => f.docs.module === module).flatMap(f => f.docs.items.filter(i => !i.isPrivate || module === here));
}

/** The fields of every record [file] sees, for `x.` without the compiler, which knows x's type. */
export function allMembers(project: Project, file: string): DocItem[] {
    return project.closure(file).flatMap(f => typesOf(f.docs).flatMap(t => t.kind === "ENUM" ? [] : t.members));
}

/** The modules [file] sees. */
export function moduleNames(project: Project, file: string): string[] {
    return [...new Set(project.closure(file).map(f => f.docs.module).filter((m): m is string => m !== null))];
}
