import { isBlank, lines, trimIndent } from "./kotlin";

// A port of src/main/kotlin/github/mrh0/eclang/doc/Ecdoc.kt: the same comment gives the same text and the same problems
// in the editor as from `eclang doc`. Change the two together; `npm test` compares them.

/** A place in a file: [line] is 1-based, [column] 0-based, in UTF-16 units. */
export interface Loc { line: number; column: number; }

/** Something wrong with an ecdoc: a warning, as the comment is only documentation. */
export interface DocProblem { location: Loc; message: string; }

/** A block tag: `@name arg text`. [arg] is the parameter of `@param` and the type of `@throws`, when written. */
export interface DocTag { name: string; location: Loc; arg: string | null; text: string; }

export const PARAM = "param", RETURN = "return", THROWS = "throws", SEE = "see", SINCE = "since", DEPRECATED = "deprecated", AUTHOR = "author";

/** Tag names and the names they stand for (`@returns` is `@return`, `@exception` is `@throws`). */
const known = new Map([[PARAM, PARAM], [RETURN, RETURN], ["returns", RETURN], [THROWS, THROWS], ["exception", THROWS],
    [SEE, SEE], [SINCE, SINCE], [DEPRECATED, DEPRECATED], [AUTHOR, AUTHOR]]);

/** An ecdoc, `/** ... *\/`: a Markdown description, then block tags. Types are never written in it: they come from the declaration. */
export class Ecdoc {
    constructor(readonly location: Loc, readonly description: string, readonly tags: DocTag[]) {}

    all(name: string): DocTag[] { return this.tags.filter(t => t.name === name); }
    first(name: string): DocTag | undefined { return this.tags.find(t => t.name === name); }

    /**
     * Parses [raw], the text of an ecdoc (with its delimiters) that starts at [line]:[column], for a declaration that
     * throws the types [thrown] (`@throws Name text` names one of them; otherwise the whole text describes the error).
     */
    static parse(raw: string, line: number, column: number, problems: DocProblem[], thrown: string[] = []): Ecdoc {
        const description: string[] = [];
        const tags: DocTag[] = [];
        let current: { name: string; line: number; text: string[] } | null = null;
        const finish = () => {
            if (current === null) return;
            const more = trimIndent(current.text.slice(1).join("\n")); // the lines after the tag's, without their indentation
            tags.push(tag(current.name, { line: line + current.line, column: 0 }, (current.text[0] + "\n" + more).trimEnd(), problems, thrown));
            current = null;
        };
        let fenced = false;
        strip(raw).forEach((text, i) => {
            const match = fenced ? null : /^@([A-Za-z]\w*)\s*(.*)$/.exec(text.trimStart());
            if (text.trimStart().startsWith("```") || text.trimStart().startsWith("~~~")) fenced = !fenced;
            if (match !== null) {
                finish();
                current = { name: match[1], line: i, text: [match[2]] };
            } else ((current as { text: string[] } | null)?.text ?? description).push(text);
        });
        finish();
        return new Ecdoc({ line, column }, description.join("\n").replace(/^\n+|\n+$/g, "").trimEnd(), tags);
    }
}

function tag(written: string, at: Loc, text: string, problems: DocProblem[], thrown: string[]): DocTag {
    const name = known.get(written);
    if (name === undefined) {
        const names = [...known.keys()].map(k => `\`@${k}\``).join(", ");
        problems.push({ location: at, message: `Unknown ecdoc tag \`@${written}\`. The tags are ${names}. ` +
            `To start a line of the description with \`@\` (a pointer type, say), put it in backquotes: \`\` \`@${written}\` \`\`.` });
        return { name: written, location: at, arg: null, text };
    }
    if (name !== PARAM && name !== THROWS) return { name, location: at, arg: null, text: text.trim() };
    // `@param name text`, and `@throws Type text` when the declaration throws Type.
    const first = /^(<\w+>|[\w.]+)/.exec(text)?.[0] ?? null;
    const arg = first !== null && (name === PARAM || thrown.includes(first)) ? first : null;
    if (name === PARAM && arg === null)
        problems.push({ location: at, message: "`@param` needs the parameter's name first: `@param count how many to read`." });
    return { name, location: at, arg, text: (arg !== null ? text.slice(arg.length) : text).trim() };
}

/**
 * The lines of an ecdoc without its delimiters and the `*` that starts each line (and one space after it). Without such
 * stars, the lines lose their common indentation. Indentation beyond that is kept, for Markdown code blocks.
 */
function strip(raw: string): string[] {
    const onlyStars = (s: string) => /^\**$/.test(s);
    const all = lines(raw.replace(/^\/\*\*/, "").replace(/\*\/$/, "")).map(l => l.trimEnd());
    const head = all[0].trimStart();
    const first = onlyStars(head) ? "" : head;
    const rest = all.slice(1).map(l => !isBlank(l) && onlyStars(l.trim()) ? "" : l);
    const written = rest.filter(l => !isBlank(l));
    if (written.every(l => l.trimStart().startsWith("*")))
        return [first, ...rest.map(l => l.trimStart().replace(/^\*/, "").replace(/^ /, ""))];
    const indent = Math.min(...written.map(l => l.length - l.trimStart().length));
    return [first, ...rest.map(l => isBlank(l) ? "" : l.slice(indent))];
}
