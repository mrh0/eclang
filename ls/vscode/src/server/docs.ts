import { ParserRuleContext, TerminalNode, Token } from "antlr4";
import EclangParser, * as P from "./antlr/EclangParser";
import { DocProblem, Ecdoc, Loc, PARAM, RETURN, THROWS } from "./ecdoc";

// A port of src/main/kotlin/github/mrh0/eclang/doc/DocExtractor.kt, plus what hovering needs to know about the inside of
// functions (`params`, `scopes`). Change the two together; `npm test` compares them.

/** What a documented declaration is. */
export type DocKind = "FUNCTION" | "C_FUNCTION" | "RECORD" | "C_RECORD" | "C_STRUCT" | "INTERFACE" | "TYPE" | "ENUM"
    | "CONSTANT" | "VALUE" | "VARIABLE" | "C_VALUE" | "FIELD" | "METHOD" | "ENUM_VALUE";

/**
 * A declaration, with its ecdoc (or null) and its [signature] as written (a function's is rebuilt as
 * `fn name(params): R throws E`, `declare fn` included). [members] are a record's fields, an interface's fields and
 * methods, and an enum's values. [cName] is the C name of a `declare`d function.
 */
export interface DocItem {
    kind: DocKind;
    name: string;
    signature: string;
    location: Loc;
    doc: Ecdoc | null;
    isPrivate: boolean;
    members: DocItem[];
    cName: string | null;
}

/** A function's lines, and the names that mean something else inside it than outside. */
export interface FunctionScope {
    item: DocItem;
    firstLine: number;
    lastLine: number;
    /** Each parameter's name, with its declaration as written and where its name is. */
    params: Map<string, { written: string; location: Loc }>;
    /** Each local's name, with everywhere it's declared (blocks can reuse a name). */
    locals: Map<string, Loc[]>;
}

/** A `use "path"` in a file. */
export interface Use { path: string; location: Loc; }

/**
 * The documentation of one file: the declarations in source order, the module it belongs to (null for the top level),
 * the [overview] (the ecdoc before `module "name"`, or before the first `use`) and the problems found.
 */
export interface FileDocs {
    module: string | null;
    /** Where `module "name"` has its name. */
    moduleLocation: Loc | null;
    overview: Ecdoc | null;
    items: DocItem[];
    problems: DocProblem[];
    scopes: FunctionScope[];
    uses: Use[];
}

const loc = (t: Token): Loc => ({ line: t.line, column: t.column });
const content = (s: string) => s.slice(1, s.length - 1);

function closest(name: string, candidates: string[]): string | null {
    const distance = (a: string, b: string) => {
        const d = Array.from({ length: a.length + 1 }, (_, i) => Array.from({ length: b.length + 1 }, (_, j) => i === 0 ? j : j === 0 ? i : 0));
        for (let i = 1; i <= a.length; i++) for (let j = 1; j <= b.length; j++)
            d[i][j] = Math.min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + (a[i - 1] === b[j - 1] ? 0 : 1));
        return d[a.length][b.length];
    };
    let best: string | null = null, bestDistance = Infinity;
    for (const c of candidates) {
        const d = distance(name.toLowerCase(), c.toLowerCase());
        if (d < bestDistance) { best = c; bestDistance = d; }
    }
    return bestDistance <= 2 ? best : null;
}

/**
 * Attaches ecdocs to the declarations of a parsed file. The lexer sets every ecdoc aside, keyed by the start offset of the
 * token after it; a declaration takes the one before its first token. Comments that end up attached to nothing are
 * reported, as are tags that don't fit the declaration.
 */
export class DocExtractor {
    private readonly problems: DocProblem[] = [];
    private readonly used = new Set<number>();
    private readonly scopes: FunctionScope[] = [];

    constructor(private readonly text: string, private readonly docs: Map<number, Token>, private readonly strays: Token[]) {}

    extract(program: P.ProgramContext): FileDocs {
        const items: DocItem[] = [];
        const uses: Use[] = [];
        let module: string | null = null;
        let moduleLocation: Loc | null = null;
        let overview: Ecdoc | null = null;
        let seenUse = false;
        const add = (item: DocItem | null) => { if (item !== null) items.push(item); };
        for (const child of program.children ?? []) {
            if (child instanceof P.FuncContext) items.push(this.function(child, child.start, false));
            else if (child instanceof P.PrivateFunctionContext) items.push(this.function(child.func(), child.start, true));
            else if (child instanceof P.PrivateGlobalContext) add(this.global(child.global(), child.start, true));
            else if (child instanceof P.GlobalNamespaceContext) {
                module = content(child._name.text);
                moduleLocation = loc(child._name);
                overview = this.doc(child.start, "the module") ?? overview;
            } else if (child instanceof P.GlobalContext) add(this.global(child, child.start, false));
            else if (child instanceof P.UseContext) {
                uses.push({ path: content(child._from_.text), location: loc(child._from_) });
                if (!seenUse) {
                    seenUse = true;
                    // An ecdoc before the first `use` describes the file, unless `module` has one.
                    const doc = this.doc(child.start, "the description of the file");
                    if (doc !== null && overview === null) overview = doc;
                }
            }
        }
        for (const [start, token] of this.docs) if (!this.used.has(start)) this.stray(token);
        this.strays.forEach(t => this.stray(t));
        const problems = this.problems.map((p, i) => [p, i] as const)
            .sort(([a, i], [b, j]) => a.location.line - b.location.line || a.location.column - b.location.column || i - j).map(([p]) => p);
        return { module, moduleLocation, overview, items, problems, scopes: this.scopes, uses };
    }

    private stray(token: Token): void {
        this.problems.push({ location: loc(token), message: "This ecdoc doesn't document anything. Put it right before a declaration " +
            "(`fn`, `rec`, `inter`, `val`, a record field, ...), before `module` or the first `use` to describe the file, " +
            "or write `/*` instead of `/**` to make it an ordinary comment." });
    }

    /** The ecdoc before [start], parsed for a declaration of the kind [what] (named in the problems). */
    private doc(start: Token, what: string): Ecdoc | null {
        const token = this.docs.get(start.start);
        if (token === undefined) return null;
        this.used.add(start.start);
        const doc = Ecdoc.parse(token.text, token.line, token.column, this.problems, []);
        // `@param <T>` documents a type parameter, which a generic record has too.
        for (const tag of doc.tags) if ((tag.name === PARAM && !(what === "a record" && tag.arg?.startsWith("<"))) || tag.name === RETURN || tag.name === THROWS)
            this.problems.push({ location: tag.location, message: `\`@${tag.name}\` documents a function, but this is ${what}.${
                what === "a record" || what === "an interface" ? " Document each field with an ecdoc right before it." : ""}` });
        return doc;
    }

    private function(f: P.FuncContext, start: Token, isPrivate: boolean): DocItem {
        const form = f as P.FunctionDeclareContext | P.FunctionBlockContext | P.FunctionInlineContext;
        const { _name: name, _params: params } = form;
        // `fn f(): T throws E` parses either way: as `throws` after the type or as a type `T throws E`.
        let returns: P.TypeContext | null = form._returnType ?? null;
        let throws: P.TypeContext | null = form._throwing ?? null;
        if (throws === null && returns instanceof P.TypeThrowsContext) {
            throws = returns._throwing;
            returns = returns._left;
        }
        const thrown = throws === null ? [] : this.source(throws).split("|").map(t => t.trim().replace(/^\(/, "").replace(/\)$/, "").trim());
        const signature = (f.getChild(0).getText() === "inline" ? "inline " : "") + `fn ${name.text}(${params.map(p => this.source(p)).join(", ")})` +
            (returns !== null ? `: ${this.source(returns)}` : "") + (throws !== null ? ` throws ${this.source(throws)}` : "");

        const token = this.docs.get(start.start);
        let doc: Ecdoc | null = null;
        if (token !== undefined) {
            this.used.add(start.start);
            doc = Ecdoc.parse(token.text, token.line, token.column, this.problems, thrown);
            this.checkTags(doc, name.text, params, returns, throws);
        }
        const cName = f instanceof P.FunctionDeclareContext ? (f._externalName ? content(f._externalName.text) : f._name.text) : null;
        const item: DocItem = { kind: cName !== null ? "C_FUNCTION" : "FUNCTION", name: name.text, signature, location: loc(name), doc, isPrivate, members: [], cName };
        this.scopes.push(this.scope(f, item, params));
        return item;
    }

    private checkTags(doc: Ecdoc, fnName: string, params: P.ParameterContext[], returns: P.TypeContext | null, throws: P.TypeContext | null): void {
        const names = params.flatMap(p => paramName(p) ?? []);
        const generics = [...new Set(params.flatMap(p => [...this.source(p).matchAll(/(?<![\w>])<(\w+)>/g)].map(m => `<${m[1]}>`)))];
        const fn = `'${fnName}'`;
        const documented = new Set<string>();
        for (const tag of doc.all(PARAM)) {
            const arg = tag.arg;
            if (arg === null) continue;
            if (documented.has(arg)) this.problems.push({ location: tag.location, message: `\`@param ${arg}\` is written twice; merge the two into one.` });
            else if (!names.includes(arg) && !generics.includes(arg)) {
                const known = [...names, ...generics];
                const near = closest(arg, known);
                const help = near !== null ? ` Did you mean '${near}'?` : names.length === 0 ? " It has no parameters." : ` Its parameters are ${known.map(n => `'${n}'`).join(", ")}.`;
                this.problems.push({ location: tag.location, message: `\`@param ${arg}\`: ${fn} has no parameter '${arg}'.${help}` });
            }
            documented.add(arg);
        }
        const returnTags = doc.all(RETURN);
        if (returnTags.length > 1) this.problems.push({ location: returnTags[1].location, message: "`@return` is written twice; merge the two into one." });
        if (returns === null && returnTags.length > 0) this.problems.push({ location: returnTags[0].location,
            message: `\`@return\`: ${fn} returns nothing. Remove the tag, or give ${fn} a return type (\`fn ${fnName}(...): Int\`).` });
        const throwTags = doc.all(THROWS);
        if (throws === null && throwTags.length > 0) this.problems.push({ location: throwTags[0].location,
            message: `\`@throws\`: ${fn} doesn't throw. Remove the tag, or add the error type after the return type (\`: Int throws Error\`).` });
    }

    private scope(f: P.FuncContext, item: DocItem, params: P.ParameterContext[]): FunctionScope {
        const named = new Map<string, { written: string; location: Loc }>();
        for (const p of params) {
            const name = paramToken(p);
            if (name !== null) named.set(name.text, { written: this.source(p), location: loc(name) });
        }
        const locals = new Map<string, Loc[]>();
        collectLocals(f, locals);
        return { item, firstLine: f.start.line, lastLine: f.stop?.line ?? f.start.line, params: named, locals };
    }

    private global(g: P.GlobalContext, start: Token, isPrivate: boolean): DocItem | null {
        const item = (kind: DocKind, name: Token, what: string, signature = this.source(g), members: DocItem[] = []): DocItem =>
            ({ kind, name: name.text, signature, location: loc(name), doc: this.doc(start, what), isPrivate, members, cName: null });
        const value = (kind: DocKind, what: string) => {
            const name = g.getToken(EclangParser.NAME, 0).symbol;
            return item(kind, name, `${what} '${name.text}'`);
        };
        const member = (kind: DocKind, name: Token, signature: string, docStart: Token, what: string): DocItem =>
            ({ kind, name: name.text, signature, location: loc(name), doc: this.doc(docStart, what), isPrivate: false, members: [], cName: null });
        const fields = (names: Token[], types: P.TypeContext[]) =>
            names.map((n, i) => member("FIELD", n, `${n.text}: ${this.source(types[i])}`, n, "a record field"));

        if (g instanceof P.GlobalDefineContext || g instanceof P.GlobalDefineTypedContext
            || g instanceof P.GlobalDefineVolatileContext || g instanceof P.GlobalDefineVolatileTypedContext) return value("VARIABLE", "the variable");
        if (g instanceof P.GlobalDefineConstContext || g instanceof P.GlobalDefineConstTypedContext) return value("VALUE", "the value");
        if (g instanceof P.GlobalConstantContext) return value("CONSTANT", "the constant");
        if (g instanceof P.GlobalDeclareDefineContext) return item("C_VALUE", g._name, `the C variable '${g._name.text}'`);
        if (g instanceof P.GlobalDeclareConstContext) return item("C_VALUE", g._name, `the C value '${g._name.text}'`);
        if (g instanceof P.GlobalTypeDefineContext) {
            const name = g.NAME().symbol;
            return item("TYPE", name, `the type '${name.text}'`);
        }
        if (g instanceof P.GlobalTypeAliasContext) return item("TYPE", g._name, `the type '${g._name.text}'`);
        if (g instanceof P.GlobalEnumDeclareContext)
            return item("ENUM", g._name, `the enum '${g._name.text}'`, this.header(g), g._atoms.map((a, i) =>
                member("ENUM_VALUE", a, `${a.text} = ${g._constants[i].text}`, a, "an enum value")));
        if (g instanceof P.GlobalRecordDefineContext)
            return item("RECORD", g._name, "a record", this.header(g), g._fields.map(f =>
                member("FIELD", f._name, this.source(f), f.start, "a record field")));
        if (g instanceof P.GlobalRecordDeclareDefineContext) return item("C_RECORD", g._name, "a record", this.header(g), fields(g._names, g._types));
        if (g instanceof P.GlobalRecordDeclareContext) return item("C_RECORD", g._name, "a record", this.header(g));
        if (g instanceof P.GlobalStructDeclareDefineContext) return item("C_STRUCT", g._name, "a record", this.header(g), fields(g._names, g._types));
        if (g instanceof P.GlobalStructDeclareContext) return item("C_STRUCT", g._name, "a record", this.header(g));
        if (g instanceof P.GlobalInterfaceDefineContext)
            return item("INTERFACE", g._name, "an interface", this.header(g), g._members.map(m =>
                member(m instanceof P.InterfaceMethodContext ? "METHOD" : "FIELD", m.getToken(EclangParser.NAME, 0).symbol, this.source(m), m.start, "an interface member")));
        return null; // `unit` (unsupported)
    }

    /** The source text of [ctx], with runs of spaces and line breaks made one space (a newline ends a declaration anyway). */
    private source(ctx: ParserRuleContext): string {
        const last = [...(ctx.children ?? [])].reverse().find(c => !isLayout(c));
        return this.between(ctx.start, last === undefined ? ctx.stop! : stopOf(last));
    }

    /** The source text of [ctx] up to its `as` (`rec Point extends Shape`). */
    private header(ctx: ParserRuleContext): string {
        const children = ctx.children ?? [];
        const end = children.findIndex(c => (c instanceof TerminalNode && c.getText() === "as") || isLayout(c));
        return this.between(ctx.start, stopOf(children[(end < 0 ? children.length : end) - 1]));
    }

    private between(start: Token, stop: Token): string {
        return this.text.slice(start.start, stop.stop + 1).trim().replace(/\s*\n\s*/g, " ");
    }
}

function isLayout(node: unknown): boolean {
    if (!(node instanceof TerminalNode)) return false;
    const type = node.symbol.type;
    return type === EclangParser.NL || type === EclangParser.INDENT || type === EclangParser.DEDENT;
}

function stopOf(node: unknown): Token {
    return node instanceof ParserRuleContext ? node.stop! : (node as TerminalNode).symbol;
}

function paramToken(p: P.ParameterContext): Token | null {
    if (p instanceof P.ParameterVariableContext || p instanceof P.ParameterConstContext || p instanceof P.ParameterVolatileContext)
        return paramToken(p.parameter());
    return p.getToken(EclangParser.NAME, 0)?.symbol ?? null;
}

const paramName = (p: P.ParameterContext): string | null => paramToken(p)?.text ?? null;

/** The names that [node] and what's under it bind: variables, loop variables, `catch` and lambda parameters. */
function collectLocals(node: ParserRuleContext, into: Map<string, Loc[]>): void {
    const add = (t: Token) => { into.set(t.text, [...(into.get(t.text) ?? []), loc(t)]); };
    const name = (n: ParserRuleContext) => { const t = n.getToken(EclangParser.NAME, 0); if (t) add(t.symbol); };
    if (node instanceof P.StatementForInContext) {
        if (node._index) add(node._index);
        if (node._name) add(node._name);
    } else if (node instanceof P.StatementPoolContext) {
        if (node._name) add(node._name);
    } else if (node instanceof P.StatementDefineVarContext || node instanceof P.StatementDefineValContext || node instanceof P.StatementDefineVolContext
        || node instanceof P.StatementDefineConstContext || node instanceof P.StatementDefineVarTypedContext || node instanceof P.StatementDefineValTypedContext
        || node instanceof P.StatementDefineVolTypedContext || node instanceof P.StatementDefineConstTypedContext || node instanceof P.StatementDeclareVarContext
        || node instanceof P.StatementWhenContext || node instanceof P.StatementLetIfContext || node instanceof P.StatementForInRangeContext
        || node instanceof P.StatementTryCatchContext || node instanceof P.LambdaParameterContext || node instanceof P.DestructurePartContext) name(node);
    for (const child of node.children ?? []) if (child instanceof ParserRuleContext) collectLocals(child, into);
}
