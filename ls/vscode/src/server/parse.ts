import { CharStream, CommonTokenStream, DefaultErrorStrategy, ErrorListener, InputMismatchException, Interval, Parser, RecognitionException, Token } from "antlr4";
import EclangLexer from "./antlr/EclangLexer";
import EclangParser from "./antlr/EclangParser";
import { DocExtractor, FileDocs } from "./docs";
import { lines } from "./kotlin";
import { IndentingLexer } from "./lexer";
import { explain, Hint, Problem } from "./syntaxHints";

/** The result of parsing a file: its documentation, or its first syntax error. */
export type Parsed = { docs: FileDocs; error: null } | { docs: null; error: Hint };

/** Thrown from the listener to leave the parser: the errors after the first are mostly ANTLR losing track. */
class Stop extends Error {}

/** Keeps the first syntax error, as `Compiler.SyntaxErrors` does. */
class FirstError extends ErrorListener<any> {
    hint: Hint | null = null;
    constructor(private readonly source: string[]) { super(); }

    override syntaxError(recognizer: any, offending: any, line: number, column: number, msg: string, _e: RecognitionException | undefined): void {
        this.hint ??= explain(new Problem(this.source, line, column, offending ?? null, msg ?? ""));
        // The lexer prints what it doesn't expect, so only leave from the parser. The lexer carries on after the bad character.
        if (recognizer instanceof EclangParser) throw new Stop();
    }
}

/**
 * The JavaScript runtime records where the expected tokens were last known (`nextTokensContext`) but doesn't use it, unlike
 * the Java one. Without it `expecting` lists too little: SyntaxHints would see `expecting INDENT` for `for i in 0 to 3 do`.
 */
class JavaErrorStrategy extends DefaultErrorStrategy {
    override recoverInline(recognizer: Parser): Token {
        try {
            return super.recoverInline(recognizer);
        } catch (e) {
            const saved = this as unknown as { nextTokensContext: unknown; nextTokensState: number };
            if (e instanceof InputMismatchException && saved.nextTokensContext) {
                const exception = e as unknown as { ctx: unknown; offendingState: number };
                exception.ctx = saved.nextTokensContext;
                exception.offendingState = saved.nextTokensState;
            }
            throw e;
        }
    }
}

/**
 * In the JavaScript runtime `getText` first reads every token up to the end of the file, so a lexer error further down is
 * reported before the parser error being worded. The Java one reads up to the end of the text asked for.
 */
class JavaTokenStream extends CommonTokenStream {
    override getText(interval: Interval): string {
        const stream = this as unknown as { lazyInit(): void; sync(i: number): boolean; tokens: Token[] };
        const index = (x: unknown) => x instanceof Token ? x.tokenIndex : x as number;
        const start = index(interval.start), stop = index(interval.stop);
        if (start < 0 || stop < 0) return "";
        stream.lazyInit();
        stream.sync(stop);
        let text = "";
        for (const t of stream.tokens.slice(start, stop + 1)) {
            if (t.type === Token.EOF) break;
            text += t.text;
        }
        return text;
    }
}

/** Parses [text], the way `Compiler.parse` does. */
export function parse(text: string): Parsed {
    const errors = new FirstError(lines(text));
    const lexer = new IndentingLexer(new CharStream(text)); // In UTF-16 units, like the compiler and LSP positions.
    lexer.removeErrorListeners();
    lexer.addErrorListener(errors);
    const parser = new EclangParser(new JavaTokenStream(lexer));
    (parser as unknown as { _errHandler: DefaultErrorStrategy })._errHandler = new JavaErrorStrategy();
    parser.removeErrorListeners();
    parser.addErrorListener(errors);
    let tree;
    try {
        tree = parser.program();
    } catch (e) {
        if (!(e instanceof Stop)) throw e;
    }
    if (errors.hint !== null) return { docs: null, error: errors.hint };
    return { docs: new DocExtractor(text, lexer.ecdocs, lexer.strayEcdocs).extract(tree!), error: null };
}

/** The tokens of [text], for finding what's under the cursor. It may not parse; what can't be read is left out. */
export function tokenize(text: string): Token[] {
    const lexer = new EclangLexer(new CharStream(text));
    lexer.removeErrorListeners();
    const tokens: Token[] = [];
    for (let t = lexer.nextToken(); t.type !== Token.EOF; t = lexer.nextToken()) if (t.channel === Token.DEFAULT_CHANNEL) tokens.push(t);
    return tokens;
}
