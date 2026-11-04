import { Token } from "antlr4";
import EclangParser from "./antlr/EclangParser";
import { isBlank, replaceFirst } from "./kotlin";

// A port of src/main/kotlin/github/mrh0/eclang/SyntaxHints.kt, so a syntax error reads the same in the editor as from the
// compiler. Change the two together; `npm test` compares them.

/** A syntax error at [line] (1-based) and [column] (0-based) of [lines]. [token]: the offending token (null for the lexer). */
export class Problem {
    constructor(readonly lines: string[], readonly line: number, readonly column: number, readonly token: Token | null, readonly antlr: string) {}

    get source(): string { return this.lines[this.line - 1] ?? ""; }
    get code(): string { return this.source.trim(); }
    /** The first word of the line: `fn`, `if`, `val`, ... */
    get keyword(): string { return /^[A-Za-z_]\w*/.exec(this.code)?.[0] ?? ""; }
    /** The source from the error onwards. */
    get rest(): string { return this.source.slice(this.column); }
    /** The source from just before the error (`a++` fails at its second `+`). */
    get around(): string { return this.source.slice(Math.max(0, this.column - 1)); }
    get type(): number { return this.token?.type ?? Token.INVALID_TYPE; }
    /** The offending token is the end of a line (or of the file). */
    get atLineEnd(): boolean {
        const text = this.token?.text;
        return this.type === EclangParser.NL || this.type === Token.EOF || text?.startsWith("\n") === true || text?.startsWith("\r") === true;
    }
    /** What ANTLR expected instead: `expecting {'as', 'extends'}` or `expecting NL`. */
    get expected(): string[] {
        const list = /expecting \{(.*)}\n?$/.exec(this.antlr)?.[1] ?? /expecting (\S+)\n?$/.exec(this.antlr)?.[1];
        if (list === undefined) return [];
        return list.split(", ").map(it => it.trim().replace(/^'(.*)'$/s, "$1"));
    }
    /** The character the lexer couldn't read, for "token recognition error at: ';'". */
    get badText(): string | null { return /token recognition error at: '(.*)'\n?$/s.exec(this.antlr)?.[1] ?? null; }
    indentOf(line: number): number {
        const text = this.lines[line - 1] ?? "";
        return text.length - text.replace(/^ +/, "").length;
    }
}

/** Where to report a problem, and what to say. */
export interface Hint { line: number; column: number; message: string; }

const hint = (line: number, column: number, message: string): Hint => ({ line, column, message });
const at = (p: Problem, message: string): Hint => hint(p.line, p.column, message);

/** `for (a, b) in ...`, which isn't the C-style `for` it starts like. */
const destructuringFor = /^for\s*\([\w\s,()]*\)\s*in\b/;

const blockKeywords = ["fn", "if", "else", "while", "for", "inline", "defer"];
const declarationKeywords = "`fn`, `val`, `var`, `const`, `rec`, `inter`, `use`, `module` or `declare`";
const CLOSE: Record<string, string> = { "(": ")", "[": "]", "{": "}" };

/** The table of common mistakes, most specific first. Each returns null when it doesn't apply. */
const rules: ((p: Problem) => Hint | null)[] = [
    // Tabs: the lexer only counts spaces as indentation, so a tab-indented line looks unindented.
    p => {
        const tabLine = p.lines.slice(0, p.line + 1).findIndex(l => !isBlank(l) && /^[ \t]*/.exec(l)![0].includes("\t"));
        if (tabLine < 0) return null;
        return hint(tabLine + 1, 0, "This line is indented with a tab. eclang only counts spaces as indentation, so the blocks can't be read. " +
            "Indent with spaces (4 per level); most editors can convert tabs to spaces.");
    },
    // Characters the lexer can't read.
    p => {
        const bad = p.badText;
        if (bad === null) return null;
        if (bad === ";") return at(p, "eclang doesn't end statements with `;`. Remove it: a new line ends a statement.");
        if (bad.startsWith("\"")) return at(p, "This string has no closing `\"`. A string must end on the line it starts on.");
        if (bad.startsWith("'")) {
            const text = /^'([^']*)'?/.exec(p.rest)?.[1] ?? bad.slice(1);
            return at(p, `Single quotes hold one character (\`'a'\`). Write text in double quotes: \`"${text}"\`.`);
        }
        return at(p, `eclang doesn't use the character \`${bad.trim()}\` here.`);
    },
    // A line that ends a block header without `do` (or `as` for records and interfaces).
    p => {
        if (p.type !== EclangParser.INDENT) return null;
        if (blockKeywords.includes(p.keyword)) {
            if (p.code.endsWith(":")) return at(p, `A block starts with \`do\` in eclang, not \`:\`. Write \`${p.code.slice(0, -1).trimEnd()} do\`.`);
            return at(p, `The indented block below needs a \`do\` at the end of this line: \`${p.code} do\`.`);
        }
        if (["rec", "record", "inter"].includes(p.keyword)) return at(p, `The fields go in an indented block after \`as\`: \`${p.code} as\`.`);
        return null;
    },
    p => {
        if (["rec", "record"].includes(p.keyword) && p.expected.includes("as"))
            return at(p, `A record's fields follow \`as\`, not \`${p.token?.text}\`: \`${p.keyword} Name as\`, then one \`name: Type\` per indented line.`);
        return null;
    },
    // An unexpected indented line: deeper than its block, or not lined up with any block when going back out.
    p => {
        if (p.type !== EclangParser.INDENT) return null;
        const next = p.line + 1;
        const indent = p.indentOf(next);
        if (indent > p.indentOf(p.line)) return hint(next, indent, "This line is indented more than the line before it, but that line doesn't start a block " +
            "(a block follows a line ending in `do`). Indent it like the line before.");
        // The indentation of the blocks this line could belong to, from the innermost out.
        const levels: number[] = [];
        for (let l = p.line; l >= 1; l--) {
            if (isBlank(p.lines[l - 1])) continue;
            const i = p.indentOf(l);
            if (levels.length === 0 || i < levels[levels.length - 1]) levels.push(i);
        }
        return hint(next, indent, `This line is indented by ${indent} spaces, which doesn't line up with any block around it ` +
            `(they are indented by ${levels.join(", ")}). Indent it like the other lines of its block.`);
    },
    p => p.antlr.includes("expecting INDENT")
        ? at(p, "The block after this line is missing. Indent its statements by 4 more spaces than this line, or write `pass` (indented) for a block that does nothing.")
        : null,
    p => p.token?.text === "{" && blockKeywords.includes(p.keyword)
        ? at(p, "Blocks are indented, not written in `{ }`. End the line with `do` and indent the body by 4 spaces.")
        : null,
    // Keywords from other languages.
    p => {
        switch (p.keyword) {
            case "return": return at(p, `\`return\` is written \`ret\` in eclang: \`${replaceFirst(p.code, "return", "ret")}\`.`);
            case "let": return at(p, `Declare a value with \`val\` (it can't change) or \`var\` (it can): \`${replaceFirst(p.code, "let", "val")}\`.`);
            case "func": case "def": case "function": case "fun":
                return hint(p.line, 0, `Functions are declared with \`fn\`: \`${replaceFirst(p.code, p.keyword, "fn")}\`.`);
            case "elif": return at(p, "`elif` is written `else if` in eclang.");
            case "switch": case "match": case "case": case "when":
                return at(p, `eclang has no \`${p.keyword}\`. Use \`if ... do\`, \`else if ... do\` and \`else\`.`);
            default: return null;
        }
    },
    p => /^for\s*\(/.test(p.code) && !destructuringFor.test(p.code) ? at(p, "eclang has no C-style `for`. Loop over a range with `for i in 0..<10 do`, or use `while cond do`.") : null,
    // `val (a, b) = ...` is a statement, not a declaration.
    p => p.token?.text === "(" && ["val", "var"].includes(p.keyword) && p.source.startsWith(p.keyword)
        ? at(p, `\`${p.keyword} (a, b) = ...\` only works inside a function. At the top level, name the tuple (\`${p.keyword} t = ...\`) and read its parts with \`t.first\`, \`t.second\`.`)
        : null,
    // A C-style declaration: `Int a = 1`.
    p => {
        const m = /^([A-Z]\w*\??)\s+([a-z_]\w*)\s*=\s*(.*)$/.exec(p.code);
        return m && at(p, `Variables are declared with the type after the name: \`val ${m[2]}: ${m[1]} = ${m[3]}\` (or \`var\` to change it later).`);
    },
    p => {
        const m = /^fn\s+\w+\s*\(.*\b([a-z_]\w*)\s+([A-Z@\[]\w*)/.exec(p.code);
        return m && at(p, `Parameters are written \`name: Type\`: \`${m[1]}: ${m[2]}\`.`);
    },
    // Operators from other languages.
    p => {
        if (p.around.startsWith("++") || p.around.startsWith("--")) return at(p, `eclang has no \`${p.around.slice(0, 2)}\`. Write \`x ${p.around[0]}= 1\`.`);
        if (p.rest.startsWith("->")) return at(p, "The return type follows a colon: `fn name(): Int do`.");
        if (p.token?.text === "=" && ["if", "while", "else", "assert"].includes(p.keyword)) return at(p, "`=` assigns a value. To compare, write `==`.");
        if (p.token?.text === "?" && p.rest.includes(":")) return at(p, "eclang has no `cond ? a : b`. Write `if cond do a else b`.");
        if (p.token?.text === ":" && blockKeywords.includes(p.keyword) && p.rest.trim() === ":")
            return at(p, `A block starts with \`do\` in eclang, not \`:\`. Write \`${p.code.slice(0, -1).trimEnd()} do\`.`);
        return null;
    },
    // Brackets.
    p => {
        const closers = [")", "]", "]c", "}"];
        if (!p.atLineEnd || (!p.expected.some(it => closers.includes(it)) && !p.antlr.startsWith("missing"))) return null;
        const open = unclosed(p.source);
        return open === null ? null : at(p, `A \`${open}\` on this line isn't closed. Add the \`${CLOSE[open]}\` before the end of the line.`);
    },
    p => {
        const text = p.token?.text;
        if (text === undefined || ![")", "]", "}"].includes(text) || !p.antlr.startsWith("extraneous")) return null;
        const open = Object.keys(CLOSE).find(k => CLOSE[k] === text);
        return at(p, `This \`${text}\` has no matching \`${open}\`. Remove it, or add the opening one.`);
    },
    p => {
        if (!p.atLineEnd || !p.expected.includes("NAME") || !p.expected.includes("INT")) return null;
        const last = /[^\p{L}\p{Nd} )\]"]*$/u.exec(p.source.trimEnd())![0];
        return at(p, `The line ends in the middle of an expression${last.length > 0 ? ` (after \`${last}\`)` : ""}. Finish it on this line.`);
    },
    // Statements at the top level of a file.
    p => p.expected.includes("<EOF>") && p.expected.includes("fn")
        ? hint(p.line, 0, `Only declarations (${declarationKeywords}) can be at the top level of a file. Put statements inside a function, such as \`fn main(): Int do\`.`)
        : null,
];

/** The innermost bracket of [line] that isn't closed (outside strings), or null. */
function unclosed(line: string): string | null {
    const stack: string[] = [];
    let inString: string | null = null;
    for (let i = 0; i < line.length; i++) {
        const c = line[i];
        if (inString !== null) {
            if (c === "\\") i++;
            else if (c === inString) inString = null;
        } else if (c === "\"" || c === "'") inString = c;
        else if (c === "/" && line[i + 1] === "/") break;
        else if (c in CLOSE) stack.push(c);
        else if (Object.values(CLOSE).includes(c) && stack.length > 0 && CLOSE[stack[stack.length - 1]] === c) stack.pop();
    }
    return stack[stack.length - 1] ?? null;
}

/** ANTLR's message without the parts that don't help: its glued-together input, and long lists of expected tokens. */
function fallback(p: Problem): string {
    const near = p.type === Token.EOF ? " at the end of the file"
        : p.type === EclangParser.INDENT ? " at an indented line"
        : p.type === EclangParser.DEDENT ? " where a block ends"
        : p.atLineEnd ? " at the end of the line"
        : p.token !== null ? ` near '${p.token.text.trim()}'`
        : "";
    const expected = p.expected.filter(it => it !== "NL").map(it => /^\p{Lu}*$/u.test(it) ? it.toLowerCase() : `\`${it}\``);
    const what = expected.length > 0 && expected.length <= 4
        ? `Expected ${expected.join(" or ")} here.`
        : "This isn't valid eclang code. Check the line against the examples in the README.";
    return `Syntax error${near}. ${what}`;
}

export function explain(p: Problem): Hint {
    for (const rule of rules) {
        const found = rule(p);
        if (found !== null) return found;
    }
    return at(p, fallback(p));
}
