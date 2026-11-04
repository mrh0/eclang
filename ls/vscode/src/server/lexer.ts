import { CommonToken, Token } from "antlr4";
import EclangLexer from "./antlr/EclangLexer";
import EclangParser from "./antlr/EclangParser";

/**
 * The lexer with what the grammar's `@lexer::members` block does in the compiler (scripts/gen-parser.mjs cuts that Java
 * block out of the grammar):
 *  - Ecdocs never reach the parser. Each is kept in [ecdocs], by the start offset of the token after it.
 *  - INDENT and DEDENT tokens come from the width of the NL tokens, as com.yuvalshavit.antlr4.DenterHelper does:
 *    [nextToken] is that class, ported.
 */
export class IndentingLexer extends EclangLexer {
    /** Each ecdoc, by the start offset of the token after it; see doc.ts. */
    readonly ecdocs = new Map<number, Token>();
    /** Ecdocs followed by another ecdoc or by the end of the file: they document nothing. */
    readonly strayEcdocs: Token[] = [];
    private pendingDoc: Token | null = null;

    private readonly dents: Token[] = []; // a queue
    private readonly indentations: number[] = []; // a stack
    private reachedEof = false;

    private pullToken(): Token {
        let t = super.nextToken();
        while (t.type === EclangLexer.ECDOC) {
            if (this.pendingDoc !== null) this.strayEcdocs.push(this.pendingDoc);
            this.pendingDoc = t;
            t = super.nextToken();
        }
        if (this.pendingDoc !== null && t.type !== EclangLexer.NL) {
            if (t.type === Token.EOF) this.strayEcdocs.push(this.pendingDoc);
            else this.ecdocs.set(t.start, this.pendingDoc);
            this.pendingDoc = null;
        }
        return t;
    }

    override nextToken(): Token {
        this.initIfFirstRun();
        const t = this.dents.length === 0 ? this.pullToken() : this.dents.shift()!;
        if (this.reachedEof) return t;
        if (t.type === EclangLexer.NL) return this.handleNewline(t);
        if (t.type === Token.EOF) return this.handleEof(t);
        return t;
    }

    private initIfFirstRun(): void {
        if (this.indentations.length > 0) return;
        this.indentations.push(0);
        let first: Token;
        do first = this.pullToken(); while (first.type === EclangLexer.NL);
        if (first.column > 0) {
            this.indentations.push(first.column);
            this.dents.push(this.inject(EclangParser.INDENT, first));
        }
        this.dents.push(first);
    }

    private handleNewline(newline: Token): Token {
        let t = newline;
        let next = this.pullToken();
        while (next.type === EclangLexer.NL) {
            t = next;
            next = this.pullToken();
        }
        if (next.type === Token.EOF) return this.handleEof(next);

        const text = t.text;
        let indent = text.length - 1; // every NL has one \n
        if (indent > 0 && text[0] === "\r") indent--;
        const previous = this.indentations[this.indentations.length - 1];
        let result: Token;
        if (indent === previous) result = t;
        else if (indent > previous) {
            result = this.inject(EclangParser.INDENT, t);
            this.indentations.push(indent);
        } else result = this.unwindTo(indent, t);
        this.dents.push(next);
        return result;
    }

    private handleEof(t: Token): Token {
        const result = this.indentations.length === 0 ? this.inject(EclangLexer.NL, t) : this.unwindTo(0, t);
        this.dents.push(t);
        this.reachedEof = true;
        return result;
    }

    /** Queues a NL and the dedents down to [target]; a line between two indentations gets an INDENT. Returns the first. */
    private unwindTo(target: number, copyFrom: Token): Token {
        this.dents.push(this.inject(EclangLexer.NL, copyFrom));
        for (;;) {
            const previous = this.indentations.pop()!;
            if (previous === target) break;
            if (target > previous) {
                this.indentations.push(previous);
                this.dents.push(this.inject(EclangParser.INDENT, copyFrom));
                break;
            }
            this.dents.push(this.inject(EclangParser.DEDENT, copyFrom));
        }
        this.indentations.push(target);
        return this.dents.shift()!;
    }

    private inject(type: number, copyFrom: Token): Token {
        const token = (copyFrom as CommonToken).clone();
        token.type = type;
        token.text = type === EclangLexer.NL ? "newline" : type === EclangParser.INDENT ? "indent" : "dedent";
        return token;
    }
}
