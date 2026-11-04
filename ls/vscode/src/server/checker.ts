import { ChildProcessWithoutNullStreams, spawn } from "node:child_process";
import { realpathSync } from "node:fs";

/** How long the compiler may take on one file, in milliseconds. */
const TIMEOUT = 30_000;
/** How many times the compiler is restarted after it stops by itself. */
const MAX_RESTARTS = 3;

/** Something the compiler said: [line] is 1-based, [column] 0-based. */
export interface CheckProblem { file: string; line: number; column: number; message: string; }
export interface CheckResult { errors: CheckProblem[]; warnings: CheckProblem[]; }

/** The two names stand for the same file. The compiler answers with canonical paths. */
export function sameFile(a: string, b: string): boolean {
    if (a === b) return true;
    try { return realpathSync(a) === realpathSync(b); } catch { return false; }
}

/**
 * Runs `eclang check` (see Checker.kt), one request at a time. [onUnavailable] is called once, with the reason for the user,
 * if it can't be run; [available] is false from then on.
 */
export class Checker {
    available = true;
    private process: ChildProcessWithoutNullStreams | null = null;
    private buffer = "";
    private errorOutput = "";
    private answer: ((line: string | null) => void) | null = null;
    private restarts = 0;

    constructor(private readonly java: string, private readonly jar: string, private readonly onUnavailable: (reason: string) => void) {}

    private start(): ChildProcessWithoutNullStreams {
        const child = spawn(this.java, ["-XX:+UseSerialGC", "-Xss8m", "-jar", this.jar, "check"], { stdio: ["pipe", "pipe", "pipe"] });
        this.buffer = "";
        this.errorOutput = "";
        child.stdout.setEncoding("utf8");
        child.stdout.on("data", (chunk: string) => {
            this.buffer += chunk;
            const end = this.buffer.indexOf("\n");
            if (end < 0) return;
            const line = this.buffer.slice(0, end);
            this.buffer = this.buffer.slice(end + 1);
            this.restarts = 0;
            this.finish(line);
        });
        child.stderr.setEncoding("utf8");
        child.stderr.on("data", (chunk: string) => { this.errorOutput = (this.errorOutput + chunk).slice(-500); });
        child.stdin.on("error", () => {}); // it stopped: "close" below says so
        child.on("error", () => this.stopped(child, `Couldn't run '${this.java}'. Install Java 17 or later, or set eclang.java.path.`));
        child.on("close", () => this.stopped(child, `The compiler stopped: ${this.errorOutput.trim() || "no reason given"}. Check eclang.java.path (Java 17 or later is needed) and eclang.compiler.jar.`));
        return child;
    }

    private stopped(child: ChildProcessWithoutNullStreams, reason: string): void {
        if (this.process !== child) return; // stopped on purpose, or reported already
        this.process = null;
        if (++this.restarts > MAX_RESTARTS || !child.pid) {
            this.available = false;
            this.onUnavailable(reason);
        }
        this.finish(null);
    }

    private finish(line: string | null): void {
        const answer = this.answer;
        this.answer = null;
        answer?.(line);
    }

    /** What the compiler says of [file], with [unsaved] instead of what's on disk; null if it can't tell. */
    async check(file: string, unsaved: { path: string; text: string }[]): Promise<CheckResult | null> {
        if (!this.available) return null;
        const child = this.process ??= this.start();
        const parts = [Buffer.from(`check ${unsaved.length} ${file}\n`)];
        for (const { path, text } of unsaved) {
            const bytes = Buffer.from(text);
            parts.push(Buffer.from(`${bytes.length} ${path}\n`), bytes);
        }
        const line = await new Promise<string | null>(done => {
            const timer = setTimeout(() => {
                this.process = null;
                child.kill();
                this.finish(null);
            }, TIMEOUT);
            this.answer = result => { clearTimeout(timer); done(result); };
            child.stdin.write(Buffer.concat(parts));
        });
        if (line === null) return null;
        const result = JSON.parse(line);
        if (result.internal !== undefined) {
            console.error(`The compiler failed on ${file}: ${result.internal}`);
            return null;
        }
        return result;
    }

    stop(): void {
        const child = this.process;
        this.process = null;
        child?.stdin.end();
    }
}
